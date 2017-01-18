package brytskyi.waitershelperclient.app.restService;


import android.os.AsyncTask;
import brytskyi.waitershelperclient.app.restService.model.AsyncTaskResult;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IAllUsersGetter;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IDishService;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import brytskyi.waitershelperclient.app.restService.utils.MyHttpServerErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.IBarmenService;
import transferFiles.service.restService.restRequstObjects.*;
import transferFiles.to.LoginLabel;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RestBarmenService extends RestCookService implements IBarmenService, IDishService, IAllUsersGetter, IOrderingService {

    public RestBarmenService(RestTemplate template, ObjectMapper mapper) {
        super(template, mapper);
        this.template = template;
        this.mapper = mapper;
        uri = UriComponentsBuilder.fromHttpUrl("http://" + host + ":" + port + "/service/barmen/").build().toUri();
    }



    @Override
    public Ordering addOrder(Ordering ordering) {
        try {
            AsyncTaskResult result = new AddOrderingTask().execute(ordering).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Product addProduct(Product product) {
        try {
            return new AddProductTask().execute(product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class AddProductTask extends AsyncTask<Product, Void, Product> {
        @Override
        protected Product doInBackground(Product... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("addProduct").build().toUri();
            return template.postForObject(builded, params[0], Product.class);
        }
    }


    public Product getProductById(int i) throws ProductByIdNotFoundException {
        return null;
    }


    public List<Product> getProductsByName(String s) {
        return null;
    }


    @Override
    public Product removeProductById(int i) throws ProductByIdNotFoundException, UserAccessException {
        try {
            AsyncTaskResult result = new RemoveProductByIdTask().execute(i).get();
            if (result.getExceptionsWrapper() != null) {
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
            } else {
                return (Product) result.getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(ProductByIdNotFoundException.class)) throw (ProductByIdNotFoundException) e;
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class RemoveProductByIdTask extends AsyncTask<Integer, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Integer... params){
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("removeProductById").queryParam("id", params[0]).build().toUri();
                Product removed = template.getForObject(builded, Product.class);
                result.setResult(result);
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


    public List<Product> getAllProducts() {
        List<Product> result = null;
        URI builded = UriComponentsBuilder.fromUri(uri).path("getAllProducts").build().toUri();
        try {
            return new GetAllProductsTask().execute(builded).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllProductsTask extends AsyncTask<URI, Void, List<Product>> {

        @Override
        protected List<Product> doInBackground(URI... params) {
            Product[] products = template.getForEntity((URI) params[0], Product[].class).getBody();
            return Arrays.asList(products);
        }
    }

    private class AddOrderingTask extends AsyncTask<Ordering, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Ordering... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("addOrder").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering added = template.postForObject(builded, params[0], Ordering.class);
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
    public Ordering addDenominationToOrder(AddDenominationToOrderRequest addDenominationToOrderRequest) {
        return null;
    }

    @Override
    public Ordering setKO(String s, Ordering ordering) {
        try {
            AsyncTaskResult result = new SetKOTask().execute(s, ordering).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class SetKOTask extends AsyncTask<Object, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Object... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("setKO/").path((String) params[0]).build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering withSetted = template.postForObject(builded, ((Ordering) params[1]), Ordering.class);
                result.setResult(withSetted);
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
    public Fund getFinalFund(Ordering ordering) {
        return null;
    }

    @Override
    public List<Ordering> getOrderings(LocalDate localDate) {
        try {
            AsyncTaskResult result = new GetOrderingsDateTask().execute(localDate).get();
            if (result.getResult() != null) return (List<Ordering>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetOrderingsDateTask extends AsyncTask<LocalDate, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(LocalDate... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getOrderings/concretteDay").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ordering> orderings = Arrays.asList(template.postForEntity(builded, params[0], Ordering[].class).getBody());
                result.setResult(orderings);
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
    public List<Ordering> getOrderings(GetByDateBeginEndRequest getByDateBeginEndRequest) {
        try {
            AsyncTaskResult result = new GetOrderingsPeriodTask().execute(getByDateBeginEndRequest).get();
            if (result.getResult() != null) return (List<Ordering>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetOrderingsPeriodTask extends AsyncTask<GetByDateBeginEndRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetByDateBeginEndRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getOrderings/period").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ordering> orderings = Arrays.asList(template.postForEntity(builded, params[0], Ordering[].class).getBody());
                result.setResult(orderings);
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
    public List<Ordering> getOrderings(GetOrderingByDateUserTakenRequest getOrderingByDateUserTakenRequest) {
        try {
            AsyncTaskResult result = new GetOrderingsDateUserTakenTask().execute(getOrderingByDateUserTakenRequest).get();
            if (result.getResult() != null) return (List<Ordering>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetOrderingsDateUserTakenTask extends AsyncTask<GetOrderingByDateUserTakenRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetOrderingByDateUserTakenRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getOrderings/userTakenConcretteDay").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ordering> orderings = Arrays.asList(template.postForEntity(builded, params[0], Ordering[].class).getBody());
                result.setResult(orderings);
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
    public List<Ordering> getOrderings(GetOrderingByDateBeginEndUserTakenRequest getOrderingByDateBeginEndUserTakenRequest) {
        try {
            AsyncTaskResult result = new GetOrderingsPeriodUserTakenTask().execute(getOrderingByDateBeginEndUserTakenRequest).get();
            if (result.getResult() != null) return (List<Ordering>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetOrderingsPeriodUserTakenTask extends AsyncTask<GetOrderingByDateBeginEndUserTakenRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetOrderingByDateBeginEndUserTakenRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getOrderings/userTakenPeriod").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ordering> orderings = Arrays.asList(template.postForEntity(builded, params[0], Ordering[].class).getBody());
                result.setResult(orderings);
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
    public List<Ordering> getOrderingsUserServes(GetOrderingByDateBeginEndUserServesRequest getOrderingByDateBeginEndUserServesRequest) {
        try {
            AsyncTaskResult result = new GetOrderingsUserServesTask().execute(getOrderingByDateBeginEndUserServesRequest).get();
            if (result.getResult() != null) return (List<Ordering>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetOrderingsUserServesTask extends AsyncTask<GetOrderingByDateBeginEndUserServesRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(GetOrderingByDateBeginEndUserServesRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getOrderings/userServesPeriod").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Ordering> orderings = Arrays.asList(template.postForEntity(builded, params[0], Ordering[].class).getBody());
                result.setResult(orderings);
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
    public Ordering updateOrdering(Ordering ordering) {
        try {
            AsyncTaskResult result = new UpdateOrderingTask().execute(ordering).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class UpdateOrderingTask extends AsyncTask<Ordering, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Ordering... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("updateOrdering").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering updated = template.postForObject(builded, params[0], Ordering.class);
                result.setResult(updated);
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
    public Ordering setWhoServesOrder(SetWhoServesOrderingRequest setWhoServesOrderingRequest) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException {
        try {
            AsyncTaskResult result = new SetWhoServesOrderTask().execute(setWhoServesOrderingRequest).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(OrderingAlreadyServingException.class)) throw (OrderingAlreadyServingException) e;
            if (e.getClass().equals(NoOrderingWithIdException.class)) throw (NoOrderingWithIdException) e;
        }
        return null;
    }

    private class SetWhoServesOrderTask extends AsyncTask<SetWhoServesOrderingRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(SetWhoServesOrderingRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("setWhoServesOrder").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering updated = template.postForObject(builded, params[0], Ordering.class);
                result.setResult(updated);
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
    public Denomination addDenomination(AddCancelDenominationRequest addCancelDenominationRequest) throws UserAccessException, NoOrderingWithIdException {
        try {
            AsyncTaskResult result = new AddDenominationTask().execute(addCancelDenominationRequest).get();
            if (result.getResult() != null) return (Denomination) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
            if (e.getClass().equals(NoOrderingWithIdException.class)) throw (NoOrderingWithIdException) e;
        }
        return null;
    }

    private class AddDenominationTask extends AsyncTask<AddCancelDenominationRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AddCancelDenominationRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("addDenomination").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Denomination added = template.postForObject(builded, params[0], Denomination.class);
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
    public List<Denomination> getDenominationsByOrder(Ordering ordering) {
        try {
            AsyncTaskResult result = new GetDenomsByOrderTask().execute(ordering).get();
            if (result.getResult() != null) return (List<Denomination>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetDenomsByOrderTask extends AsyncTask<Ordering, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Ordering... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getDenominationsByOrder").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<Denomination> added = Arrays.asList(template.postForEntity(builded, params[0], Denomination[].class).getBody());
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
    public List<Denomination> getDenominationsByOrderForFund(Ordering ordering) {
        return null;
    }

    @Override
    public Denomination removeDenomination(RemoveDenominationRequest removeDenominationRequest) throws UserAccessException {
        try {
            AsyncTaskResult result = new RemoveDenominationTask().execute(removeDenominationRequest).get();
            if (result.getResult() != null) return (Denomination) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class RemoveDenominationTask extends AsyncTask<RemoveDenominationRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(RemoveDenominationRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("removeDenomination").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Denomination removed = template.postForObject(builded, params[0], Denomination.class);
                result.setResult(removed);
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
    public Ingridient addIngridient(AddIngridientRequest addIngridientRequest) {
        try {
            return new AddIngridientTask().execute(addIngridientRequest).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class AddIngridientTask extends AsyncTask<AddIngridientRequest, Void, Ingridient> {

        @Override
        protected Ingridient doInBackground(AddIngridientRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("addIngridient").build().toUri();
            return template.postForObject(builded, params[0], Ingridient.class);
        }
    }


    @Override
    public Ingridient removeIngridientById(int i) throws IngridientWithIDNotFoundException {
        try {
            AsyncTaskResult result = new RemoveIngridientTask().execute(i).get();
            if (result.getExceptionsWrapper() != null) {
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
            } else {
                return (Ingridient) result.getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(IngridientWithIDNotFoundException.class))
                throw (IngridientWithIDNotFoundException) e;
        }
        return null;
    }

    private class RemoveIngridientTask extends AsyncTask<Integer, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Integer... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            URI builded = UriComponentsBuilder.fromUri(uri).path("removeIngridientById").queryParam("id", params[0]).build().toUri();
            try {
                Ingridient removed = template.getForObject(builded, Ingridient.class);
                result.setResult(removed);
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
    public List<Ingridient> getIngridientsByDish(Dish dish) {
        try {
            return new GetIngridientsByDishTask().execute(dish).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetIngridientsByDishTask extends AsyncTask<Dish, Void, List<Ingridient>> {

        @Override
        protected List<Ingridient> doInBackground(Dish... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getIngridientsByDish").build().toUri();
            ResponseEntity<Ingridient[]> responseEntity = template.postForEntity(builded, params[0], Ingridient[].class);
            return Arrays.asList(responseEntity.getBody());
        }
    }


    @Override
    public Dish addDish(Dish dish) {
        try {
            return new AddDishTask().execute(dish).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class AddDishTask extends AsyncTask<Dish, Void, Dish> {

        @Override
        protected Dish doInBackground(Dish... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("addDish").build().toUri();
            return template.postForObject(builded, params[0], Dish.class);
        }
    }

    @Override
    public Dish updateDish(Dish dish) {
        try {
            return new UpdateDishTask().execute(dish).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class UpdateDishTask extends AsyncTask<Dish, Void, Dish> {

        @Override
        protected Dish doInBackground(Dish... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("updateDish").build().toUri();
            return template.postForObject(builded, params[0], Dish.class);
        }
    }

    @Override
    public List<Dish> getDishesByDishType(DishType dishType) {
        try {
            return new GetDishesByDishType().execute(dishType).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetDishesByDishType extends AsyncTask<DishType, Void, List<Dish>> {

        @Override
        protected List<Dish> doInBackground(DishType... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getDishesByDishType").build().toUri();
            return Arrays.asList(template.postForEntity(builded, params[0], Dish[].class).getBody());
        }
    }

    @Override
    public List<Dish> getAllDishes() {
        try {
            return new GetAllDishesTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllDishesTask extends AsyncTask<Void, Void, List<Dish>> {

        @Override
        protected List<Dish> doInBackground(Void... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getAllDishes").build().toUri();
            return Arrays.asList(template.getForEntity(builded, Dish[].class).getBody());
        }
    }

    @Override
    public Dish removeDish(RemoveDishRequest removeDishRequest) throws UserAccessException {
        try {
            AsyncTaskResult result = new RemoveDishTask().execute(removeDishRequest).get();
            System.out.println(result.getExceptionsWrapper().getExcClass() + "\n" + result.getExceptionsWrapper().getMessage());
            if (result.getExceptionsWrapper() != null) {
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
            } else {
                return (Dish) result.getResult();
            }
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class RemoveDishTask extends AsyncTask<RemoveDishRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(RemoveDishRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("removeDish").build().toUri();
                Dish removed = template.postForObject(builded, params[0], Dish.class);
                result.setResult(removed);
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
    public Ordering removeOrdering(RemoveOrderingRequest removeOrderingRequest) throws UserAccessException {
        try {
            AsyncTaskResult result = new RemoveOrderingTask().execute(removeOrderingRequest).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class RemoveOrderingTask extends AsyncTask<RemoveOrderingRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(RemoveOrderingRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("removeOrdering").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering ordering = template.postForObject(builded, params[0], Ordering.class);
                result.setResult(ordering);
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
    public List<User> getAllUsers() {
        try {
            AsyncTaskResult result = new GetAllUsersTask().execute().get();
            if (result.getResult() != null) return (List<User>) result.getResult();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private class GetAllUsersTask extends AsyncTask<Void, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Void... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("getAllUsers").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                List<User> users = Arrays.asList(template.getForEntity(builded, User[].class).getBody());
                result.setResult(users);
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
    public Ordering setWhoServesOrderNull(SetWhoServesOrderingRequest setWhoServesOrderingRequest) throws
            OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException {
        try {
            AsyncTaskResult result = new SetWhoServesOrderNullTask().execute(setWhoServesOrderingRequest).get();
            if (result.getResult() != null) return (Ordering) result.getResult();
            ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(), result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(OrderingNotServingByYouException.class)) throw (OrderingNotServingByYouException) e;
            if (e.getClass().equals(NoOrderingWithIdException.class)) throw (NoOrderingWithIdException) e;
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
        }
        return null;
    }

    private class SetWhoServesOrderNullTask extends AsyncTask<SetWhoServesOrderingRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(SetWhoServesOrderingRequest... params) {
            URI builded = UriComponentsBuilder.fromUri(uri).path("setWhoServesOrderNull").build().toUri();
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                Ordering ordering = template.postForObject(builded, params[0], Ordering.class);
                result.setResult(ordering);
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
    public void generatePrintPdf(Ordering ordering) throws IOException, PrintingException {
        try {
            AsyncTaskResult result = new GeneratePrintTask().execute(ordering).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(IOException.class)) throw (IOException) e;
            if (e.getClass().equals(PrintingException.class)) throw (PrintingException) e;
        }
    }

    private class GeneratePrintTask extends AsyncTask<Ordering, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(Ordering... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("generatePrintPdf").build().toUri();
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
    public void cancelDenomination(AddCancelDenominationRequest addCancelDenominationRequest) throws
            UserAccessException, DenominationWithIdNotFoundException {
        try {
            AsyncTaskResult result = new CancelDenominationTask().execute(addCancelDenominationRequest).get();
            if (result.getExceptionsWrapper() != null)
                ExceptionUtils.throwException(result.getExceptionsWrapper().getExcClass(),
                        result.getExceptionsWrapper().getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            if (e.getClass().equals(UserAccessException.class)) throw (UserAccessException) e;
            if (e.getClass().equals(DenominationWithIdNotFoundException.class))
                throw (DenominationWithIdNotFoundException) e;
        }
    }

    private class CancelDenominationTask extends AsyncTask<AddCancelDenominationRequest, Void, AsyncTaskResult> {

        @Override
        protected AsyncTaskResult doInBackground(AddCancelDenominationRequest... params) {
            AsyncTaskResult result = new AsyncTaskResult();
            try {
                URI builded = UriComponentsBuilder.fromUri(uri).path("cancelDenominationServing").build().toUri();
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


}
