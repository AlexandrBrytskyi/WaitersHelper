package brytskyi.waitershelperclient.app.restService;

import android.os.AsyncTask;
import brytskyi.waitershelperclient.app.MyApplication;
import brytskyi.waitershelperclient.app.R;
import brytskyi.waitershelperclient.app.restService.model.AsyncTaskResult;
import brytskyi.waitershelperclient.app.restService.utils.MyHttpServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import transferFiles.service.restService.ICookService;
import transferFiles.service.restService.restRequstObjects.ChangeNamePassRequest;
import transferFiles.service.restService.restRequstObjects.CockCancelDenomRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;
import transferFiles.to.LoginLabel;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RestCookService implements ICookService {

    protected RestTemplate template;
    protected ObjectMapper mapper;
    protected String host = MyApplication.getAppContext().getString(R.string.host);
    protected String port = MyApplication.getAppContext().getString(R.string.port);
    protected URI uri = UriComponentsBuilder.fromHttpUrl("http://" + host + ":" + port + "/service/cook/").build().toUri();

    public RestCookService(RestTemplate template, ObjectMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }


    @Override
    public Denomination cancelDenomination(CockCancelDenomRequest cockCancelDenomRequest) throws UserAccessException {
        try {
            AsyncTaskResult result = new CancelDenominationTask().execute(cockCancelDenomRequest).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
            return (Denomination) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class CancelDenominationTask extends AsyncTask<CockCancelDenomRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(CockCancelDenomRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("cancelDenomination").build().toUri();
                Denomination den = template.postForObject(builded, params[0], Denomination.class);
                result.setResult(den);
            } catch (MyHttpServerErrorException e) {
                try {
                    result.setExceptionsWrapper(mapper.readValue(e.getHeaders().getFirst("exception"), ExceptionsWrapper.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
    }

    @Override
    public Denomination setDenomStateReady(Denomination denomination) throws UserAccessException {
        try {
            AsyncTaskResult result = new SetDenomStateReadyTask().execute(denomination).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
            return (Denomination) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class SetDenomStateReadyTask extends AsyncTask<Denomination, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Denomination... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("setDenomStateReady").build().toUri();
                Denomination den = template.postForObject(builded, params[0], Denomination.class);
                result.setResult(den);
            } catch (MyHttpServerErrorException e) {
                try {
                    result.setExceptionsWrapper(mapper.readValue(e.getHeaders().getFirst("exception"), ExceptionsWrapper.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
    }

    @Override
    public void sentUIobjectToValidator(LoginLabel loginLabel) {

    }

    @Override
    public List<Denomination> getWorkingDenoms(User user) {
        try {
            AsyncTaskResult result = new GetCurrentDenomsTask().execute("getWorkingDenoms", user).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
            return (List<Denomination>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetCurrentDenomsTask extends AsyncTask<Object, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Object... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path((String)params[0]).build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> denoms = Arrays.asList(template.postForEntity(builded, ((User)params[1]), Denomination[].class).getBody());
                result.setResult(denoms);
            } catch (MyHttpServerErrorException e) {
                try {
                    result.setExceptionsWrapper(mapper.readValue(e.getHeaders().getFirst("exception"), ExceptionsWrapper.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
    }


    @Override
    public List<Denomination> getNewDenominations(User user) {
        try {
            AsyncTaskResult result = new GetCurrentDenomsTask().execute("getNewDenominations", user).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
            return (List<Denomination>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Denomination> getMessages(User user) {
        try {
            AsyncTaskResult result = new GetCurrentDenomsTask().execute("getMessages", user).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
            return (List<Denomination>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User changeName(ChangeNamePassRequest changeNamePassRequest) {
        try {
            AsyncTaskResult result = new ChangeNameTask().execute(changeNamePassRequest).get();
            return (User) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ChangeNameTask extends AsyncTask<ChangeNamePassRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ChangeNamePassRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("changeName").build().toUri();
                result.setResult(template.postForObject(builded, params[0], User.class));
            } catch (MyHttpServerErrorException e) {
                try {
                    result.setExceptionsWrapper(mapper.readValue(e.getHeaders().getFirst("exception"), ExceptionsWrapper.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
    }

    @Override
    public User changePassword(ChangeNamePassRequest changeNamePassRequest) throws WrongPasswordException {
        try {
            AsyncTaskResult result = new ChangePassTask().execute(changeNamePassRequest).get();
            if (result.getResult() != null) return (User) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(WrongPasswordException.class)) throw (WrongPasswordException) e;
        }
        return null;
    }

    private class ChangePassTask extends AsyncTask<ChangeNamePassRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(ChangeNamePassRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("changePassword").build().toUri();
                result.setResult(template.postForObject(builded, params[0], User.class));
            } catch (MyHttpServerErrorException e) {
                try {
                    result.setExceptionsWrapper(mapper.readValue(e.getHeaders().getFirst("exception"), ExceptionsWrapper.class));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
    }
}
