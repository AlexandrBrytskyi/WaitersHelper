package brytskyi.waitershelperclient.app.restService;


import android.os.AsyncTask;
import brytskyi.waitershelperclient.app.restService.model.AsyncTaskResult;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IReportService;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IUserControl;
import brytskyi.waitershelperclient.app.restService.utils.MyHttpServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.GenerateReportRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;
import transferFiles.service.restService.restRequstObjects.LockUserRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RestAdminService extends RestWaiterService implements IAdminService, IUserControl, IOrderingService, IReportService {


    public RestAdminService(RestTemplate template, ObjectMapper mapper) {
        super(template, mapper);
        this.template = template;
        this.mapper = mapper;
        uri = UriComponentsBuilder.fromHttpUrl("http://" + host + ":" + port + "/service/admin/").build().toUri();
    }


    @Override
    public User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException {
        try {
            AsyncTaskResult result = new AddNewUserTask().execute(user).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
            return (User) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserFieldIsEmptyException.class)) throw (UserFieldIsEmptyException) e;
            if (e.getClass().equals(WrongLoginException.class)) throw (WrongLoginException) e;
        }
        return null;
    }

    private class AddNewUserTask extends AsyncTask<User, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(User... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("addNewUser").build().toUri();
                User added = template.postForObject(builded, params[0], User.class);
                result.setResult(added);
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
    public User removeUser(User user) {
        return null;
    }

    @Override
    public void lockUser(LockUserRequest lockUserRequest) {
        try {
            AsyncTaskResult result = new LockUserTask().execute(lockUserRequest).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LockUserTask extends AsyncTask<LockUserRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(LockUserRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("lockUser").build().toUri();
                template.postForObject(builded, params[0], Void.class);
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
    public List<Denomination> getSoltDenominationsForReport(LocalDate localDate) {
        try {
            AsyncTaskResult result = new GetSoltDenominationsForReportTask().execute(localDate).get();
            if (result.getResult() != null) return (List<Denomination>) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetSoltDenominationsForReportTask extends AsyncTask<LocalDate, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(LocalDate... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getSoltDenominationsForReport").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> denoms = Arrays.asList(template.postForEntity(builded, params[0], Denomination[].class).getBody());
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
    public List<Denomination> getSoltDenominationsForReport(GetByDateBeginEndRequest getByDateBeginEndRequest) {
        try {
            AsyncTaskResult result = new GetSoltDenominationsForReportPeriodTask().execute(getByDateBeginEndRequest).get();
            if (result.getResult() != null) return (List<Denomination>) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetSoltDenominationsForReportPeriodTask extends AsyncTask<GetByDateBeginEndRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetByDateBeginEndRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getSoltDenominationsForReportPeriod").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> denoms = Arrays.asList(template.postForEntity(builded, params[0], Denomination[].class).getBody());
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
    public List<Denomination> getRequireDenominationsForReport(LocalDate localDate) throws DateException {
        try {
            AsyncTaskResult result = new GetRequireDenominationsForReportTask().execute(localDate).get();
            if (result.getResult() != null) return (List<Denomination>) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(DateException.class)) throw (DateException) e;
        }
        return null;
    }

    private class GetRequireDenominationsForReportTask extends AsyncTask<LocalDate, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(LocalDate... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getRequireDenominationsForReport").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> denoms = Arrays.asList(template.postForEntity(builded, params[0], Denomination[].class).getBody());
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
    public List<Denomination> getRequireDenominationsForReport(GetByDateBeginEndRequest getByDateBeginEndRequest) throws DateException {
        try {
            AsyncTaskResult result = new GetRequireDenominationsForReportPeriodTask().execute(getByDateBeginEndRequest).get();
            if (result.getResult() != null) return (List<Denomination>) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(DateException.class)) throw (DateException) e;
        }
        return null;
    }

    private class GetRequireDenominationsForReportPeriodTask extends AsyncTask<GetByDateBeginEndRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetByDateBeginEndRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getRequireDenominationsForReportPeriod").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> denoms = Arrays.asList(template.postForEntity(builded, params[0], Denomination[].class).getBody());
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
    public List<Ingridient> countIngridientsForReport(List<Denomination> list) {
        try {
            AsyncTaskResult result = new CountIngridientsForReportTask().execute(list).get();
            if (result.getResult() != null) return (List<Ingridient>) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class CountIngridientsForReportTask extends AsyncTask<List<Denomination>, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(List<Denomination>... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("countIngridientsForReport").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ingridient> ingridients = Arrays.asList(template.postForEntity(builded, params[0], Ingridient[].class).getBody());
                result.setResult(ingridients);
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
    public File generateReport(GenerateReportRequest generateReportRequest) throws FileNotFoundException {
        try {
            AsyncTaskResult result = new GenerateReportTask().execute(generateReportRequest).get();
            if (result.getResult() != null) return (File) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GenerateReportTask extends AsyncTask<GenerateReportRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GenerateReportRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("generateReport").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                File pdf = template.postForObject(builded, params[0], File.class);
                result.setResult(pdf);
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
