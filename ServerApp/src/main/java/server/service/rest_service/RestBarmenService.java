package server.service.rest_service;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import server.persistentModel.ConvertDate;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.*;
import transferFiles.service.rmiService.IBarmenService;
import transferFiles.to.LoginLabel;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

@RestController("restBarmenService")
@RequestMapping("/service/barmen")
public class RestBarmenService implements transferFiles.service.restService.IBarmenService {

    @Autowired
    @Qualifier("barmenService")
   private IBarmenService barmenService;


    @Override
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public Ordering addOrder(@RequestBody Ordering ordering) {
        return barmenService.addOrder(ordering);
    }

    @Override
    @RequestMapping(value = "/addDenomToOrder", method = RequestMethod.POST)
    public Ordering addDenominationToOrder(@RequestBody AddDenominationToOrderRequest request) {
        return barmenService.addDenominationToOrder(request.getOrdering(), request.getDenomination());
    }

    @Override
    @RequestMapping(value = "/setKO/{ko:.+}", method = RequestMethod.POST)
    public Ordering setKO(@PathVariable("ko") String ko, @RequestBody Ordering ordering) {
        return barmenService.setKO(Double.parseDouble(ko), ordering);
    }

    @Override
    @RequestMapping(value = "/getFinalFund", method = RequestMethod.POST)
    public Fund getFinalFund(@RequestBody Ordering ordering) {
        return barmenService.getFinalFund(ordering);
    }

    @Override
    @RequestMapping(value = "/getOrderings/concretteDay", method = RequestMethod.POST)
    public List<Ordering> getOrderings(@RequestBody LocalDate concretteDay) {
        List<Ordering> result = barmenService.getOrderings(ConvertDate.toJavaFromJoda(concretteDay));
        System.out.println(result);
        return result;
    }

    @Override
    @RequestMapping(value = "/getOrderings/period", method = RequestMethod.POST)
    public List<Ordering> getOrderings(@RequestBody GetByDateBeginEndRequest request) {
        return barmenService.getOrderings(ConvertDate.toJavaFromJoda(request.getBegin()), ConvertDate.toJavaFromJoda(request.getEnd()));
    }

    @Override
    @RequestMapping(value = "/getOrderings/userTakenConcretteDay", method = RequestMethod.POST)
    public List<Ordering> getOrderings(@RequestBody GetOrderingByDateUserTakenRequest request) {
        return barmenService.getOrderings(request.getWhoTaken(), ConvertDate.toJavaFromJoda(request.getConcretteDay()));
    }

    @Override
    @RequestMapping(value = "/getOrderings/userTakenPeriod", method = RequestMethod.POST)
    public List<Ordering> getOrderings(@RequestBody GetOrderingByDateBeginEndUserTakenRequest request) {
        return barmenService.getOrderings(
                request.getWhoTaken(),
                ConvertDate.toJavaFromJoda(request.getBegin()),
                ConvertDate.toJavaFromJoda(request.getEnd())
        );
    }

    @Override
    @RequestMapping(value = "/getOrderings/userServesPeriod", method = RequestMethod.POST)
    public List<Ordering> getOrderingsUserServes(@RequestBody GetOrderingByDateBeginEndUserServesRequest request) {
        return barmenService.getOrderingsUserServes(
                request.getWhoServing(),
                ConvertDate.toJavaFromJoda(request.getBegin()),
                ConvertDate.toJavaFromJoda(request.getEnd()));
    }

    @Override
    @RequestMapping(value = "/updateOrdering", method = RequestMethod.POST)
    public Ordering updateOrdering(@RequestBody Ordering orderingSource) {
        return barmenService.updateOrdering(orderingSource);
    }

    @Override
    @RequestMapping(value = "/setWhoServesOrder", method = RequestMethod.POST)
    public Ordering setWhoServesOrder(@RequestBody SetWhoServesOrderingRequest request) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException {
        return barmenService.setWhoServesOrder(request.getOrdering(), request.getUser());
    }

    @Override
    @RequestMapping(value = "/addDenomination", method = RequestMethod.POST)
    public Denomination addDenomination(@RequestBody AddCancelDenominationRequest request) throws UserAccessException, NoOrderingWithIdException {
        return barmenService.addDenomination(request.getDenomination(), request.getLogined());
    }


    @Override
    @RequestMapping(value = "/getDenominationsByOrder", method = RequestMethod.POST)
    public List<Denomination> getDenominationsByOrder(@RequestBody Ordering ordering) {
        return barmenService.getDenominationsByOrder(ordering);
    }

    @Override
    @RequestMapping(value = "/getDenominationsByOrderForFund", method = RequestMethod.POST)
    public List<Denomination> getDenominationsByOrderForFund(@RequestBody Ordering ordering) {
        return barmenService.getDenominationsByOrderForFund(ordering);
    }

    @Override
    @RequestMapping(value = "/removeDenomination", method = RequestMethod.POST)
    public Denomination removeDenomination(@RequestBody RemoveDenominationRequest request) throws UserAccessException {
        return barmenService.removeDenomination(request.getDenomination(), request.getLogined());
    }


    @Override
    @RequestMapping(value = "/addDish", method = RequestMethod.POST)
    public Dish addDish(@RequestBody Dish dish) {
        return barmenService.addDish(dish);
    }

    @Override
    @RequestMapping(value = "/updateDish", method = RequestMethod.POST)
    public Dish updateDish(@RequestBody Dish dish) {
        return barmenService.updateDish(dish);
    }

    @Override
    @RequestMapping(value = "/getDishesByDishType", method = RequestMethod.POST)
    public List<Dish> getDishesByDishType(@RequestBody DishType dishType) {
        return barmenService.getDishesByDishType(dishType);
    }

    @Override
    @RequestMapping(value = "/getAllDishes", method = RequestMethod.GET)
    public List<Dish> getAllDishes() {
        return barmenService.getAllDishes();
    }

    @Override
    @RequestMapping(value = "/removeDish", method = RequestMethod.POST)
    public Dish removeDish(@RequestBody RemoveDishRequest request) throws UserAccessException {
        return barmenService.removeDish(request.getDish(), request.getLogined());
    }

    @Override
    @RequestMapping(value = "/removeOrdering", method = RequestMethod.POST)
    public Ordering removeOrdering(RemoveOrderingRequest request) throws UserAccessException {
        return barmenService.removeOrdering(request.getSource(), request.getLogined());
    }

    @Override
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return barmenService.getAllUsers();
    }

    @Override
    @RequestMapping(value = "/setWhoServesOrderNull", method = RequestMethod.POST)
    public Ordering setWhoServesOrderNull(@RequestBody SetWhoServesOrderingRequest request) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException {
        return barmenService.setWhoServesOrderNull(request.getOrdering(), request.getUser());
    }

    @Override
    @RequestMapping(value = "/generatePrintPdf", method = RequestMethod.POST)
    public void generatePrintPdf(@RequestBody Ordering ordering) throws IOException, PrinterException {
        barmenService.generatePrintPdf(ordering);
    }

    @Override
    @RequestMapping(value = "/cancelDenominationServing", method = RequestMethod.POST)
    public void cancelDenomination(@RequestBody AddCancelDenominationRequest request) throws UserAccessException, DenominationWithIdNotFoundException {
        barmenService.cancelDenomination(request.getLogined(), request.getDenomination());
    }

    @Override
    @RequestMapping(value = "/cancelDenominationCoocking", method = RequestMethod.POST)
    public Denomination cancelDenomination(@RequestBody CockCancelDenomRequest request) throws UserAccessException {
        return barmenService.cancelDenomination(request.getDenomination(), request.getLogined());
    }

    @Override
    @RequestMapping(value = "/setDenomStateReady", method = RequestMethod.POST)
    public Denomination setDenomStateReady(@RequestBody Denomination denomination) throws UserAccessException {
        return barmenService.setDenomStateReady(denomination);
    }

    @Override
    @RequestMapping(value = "/sentUIobjectToValidator", method = RequestMethod.POST)
    public void sentUIobjectToValidator(@RequestBody LoginLabel loginLabel) {
        barmenService.sentUIobjectToValidator(loginLabel);
    }

    @Override
    @RequestMapping(value = "/getWorkingDenoms", method = RequestMethod.POST)
    public List<Denomination> getWorkingDenoms(@RequestBody User user) {
        return barmenService.getWorkingDenoms(user);
    }

    @Override
    @RequestMapping(value = "/getNewDenominations", method = RequestMethod.POST)
    public List<Denomination> getNewDenominations(@RequestBody User user) {
        return barmenService.getNewDenominations(user);
    }

    @Override
    @RequestMapping(value = "/getMessages", method = RequestMethod.POST)
    public List<Denomination> getMessages(@RequestBody User user) {
        return barmenService.getMessages(user);
    }

    @Override
    @RequestMapping(value = "/changeName", method = RequestMethod.POST)
    public User changeName(@RequestBody ChangeNamePassRequest request) {
        return barmenService.changeName(request.getUser(), request.getValStr());
    }

    @Override
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public User changePassword(ChangeNamePassRequest request) throws WrongPasswordException {
        return barmenService.changePassword(request.getUser(), request.getValStr());
    }
}
