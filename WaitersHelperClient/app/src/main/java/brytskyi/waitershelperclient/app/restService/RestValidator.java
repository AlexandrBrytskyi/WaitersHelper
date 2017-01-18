package brytskyi.waitershelperclient.app.restService;


import android.os.AsyncTask;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.model.AsyncTaskResult;
import brytskyi.waitershelperclient.app.restService.utils.LoginLableSender;
import brytskyi.waitershelperclient.app.restService.utils.MyErrorHandler;
import brytskyi.waitershelperclient.app.restService.utils.MyHttpServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import transferFiles.exceptions.*;
import transferFiles.model.user.User;
import transferFiles.to.LoginLabel;
import transferFiles.validator.restValidator.IValidator;

import java.io.IOException;
import java.net.URI;

public class RestValidator implements IValidator {

    private RestTemplate template;
    private ObjectMapper mapper;
    private String host = MyApplication.getAppContext().getString(R.string.host);
    private String port = MyApplication.getAppContext().getString(R.string.port);
    private URI uri = UriComponentsBuilder.fromHttpUrl("http://" + host + ":" + port + "/" + "validate" + "/").build().toUri();

    public RestValidator(RestTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    @Override
    public User login(String login, String pass) throws WrongPasswordException, AccountBlockedException, WrongLoginException {
        User user = null;
        try {
            AsyncTaskResult result = new LoginAsyncTask().execute(login, pass).get();
            if (result.getResult() != null) {
                user = (User) result.getResult();
                initLoginLableSender(user);
            } else {
                ExceptionsWrapper wrapper = result.getExceptionsWrapper();
                ExceptionUtils.throwException(wrapper.getExcClass(), wrapper.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getClass().equals(WrongLoginException.class)) throw new WrongLoginException(e.getMessage());
            if (e.getClass().equals(WrongPasswordException.class)) throw new WrongPasswordException(e.getMessage());
            if (e.getClass().equals(AccountBlockedException.class)) throw new AccountBlockedException(e.getMessage());
        }
        return user;
    }

    private void initLoginLableSender(User user) {
        LoginLabel loginLabel = new LoginLabel(user, "Android client");
        Thread thread = new Thread(new LoginLableSender(loginLabel, this));
        thread.start();
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, AsyncTaskResult> {


        @Override
        protected AsyncTaskResult doInBackground(String... params) {
            AsyncTaskResult result = new AsyncTaskResult();

            URI builded = UriComponentsBuilder.fromUri(uri).path("login").queryParam("login", params[0]).queryParam("pass", params[1]).build().toUri();
            System.out.println(builded.toString() + " = uri");
            User user = null;
            ExceptionsWrapper wrapper = null;
            try {
                user = template.getForObject(builded, User.class);
                result.setResult(user);
                MyApplication.setLoginedUser(user);
            } catch (MyHttpServerErrorException e) {
                HttpHeaders headers = e.getHeaders();
                if (headers.containsKey("exception")) {
                    try {
                        wrapper = mapper.readValue(headers.getFirst("exception"), ExceptionsWrapper.class);
                        result.setExceptionsWrapper(wrapper);
                        return result;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return result;
        }
    }

    @Override
    public void sendLabelToValidator(LoginLabel loginLabel) {
        URI builded = UriComponentsBuilder.fromUri(uri).path("sendLabel").build().toUri();
        template.postForObject(builded, loginLabel, Void.class);
    }
}
