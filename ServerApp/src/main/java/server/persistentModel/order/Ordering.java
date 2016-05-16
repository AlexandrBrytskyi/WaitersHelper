package server.persistentModel.order;

import org.hibernate.LazyInitializationException;
import server.persistentModel.ConvertDate;
import server.persistentModel.denomination.Denomination;
import server.persistentModel.fund.Fund;
import server.persistentModel.user.User;
import transferFiles.model.order.OrderType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "ordering")
public class Ordering implements Serializable, Comparable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(nullable = false)
    private LocalDateTime dateOrderCreated;

    @Column(nullable = true)
    private LocalDateTime dateClientsCome;

    @Column(nullable = true)
    private int amountOfPeople;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private double advancePayment;

    @ManyToOne()
    @JoinColumn(name = "user_taken_id", referencedColumnName = "login")
    private User whoTakenOrder;


    @ManyToOne()
    @JoinColumn(name = "user_serving_id", referencedColumnName = "login")
    private User whoServesOrder;


    @OneToOne(targetEntity = Fund.class, mappedBy = "order", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Fund fund;


    @Column
    @Enumerated
    private OrderType type;


    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Denomination> denominations;

    public Ordering() {
    }

    public Ordering(int id, LocalDateTime dateOrderCreated, LocalDateTime dateClientsCome, int amountOfPeople, String description, double advancePayment, User whoTakenOrder, User whoServesOrder, OrderType type) {
        this.setId(id);
        this.dateOrderCreated = dateOrderCreated;
        this.dateClientsCome = dateClientsCome;
        this.amountOfPeople = amountOfPeople;
        this.description = description;
        this.advancePayment = advancePayment;
        this.whoTakenOrder = whoTakenOrder;
        this.whoServesOrder = whoServesOrder;
        this.type = type;
    }


    public double getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(double advancePayment) {
        this.advancePayment = advancePayment;
    }

    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateOrderCreated() {
        return dateOrderCreated;
    }

    public void setDateOrderCreated(LocalDateTime dateOrderCreated) {
        this.dateOrderCreated = dateOrderCreated;
    }

    public LocalDateTime getDateClientsCome() {
        return dateClientsCome;
    }

    public void setDateClientsCome(LocalDateTime dateClientsCome) {
    }

    public User getWhoTakenOrder() {
        return whoTakenOrder;
    }

    public void setWhoTakenOrder(User whoTakenOrder) {
        this.whoTakenOrder = whoTakenOrder;
    }

    public User getWhoServesOrder() {
        return whoServesOrder;
    }

    public void setWhoServesOrder(User whoServesOrder) {
        this.whoServesOrder = whoServesOrder;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public List<Denomination> getDenominations() {
        return denominations;
    }

    public void setDenominations(List<Denomination> denominations) {
        this.denominations = denominations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ordering ordering = (Ordering) o;

        if (id != ordering.id) return false;
        if (amountOfPeople != ordering.amountOfPeople) return false;
        if (Double.compare(ordering.advancePayment, advancePayment) != 0) return false;
        if (dateOrderCreated != null ? !dateOrderCreated.equals(ordering.dateOrderCreated) : ordering.dateOrderCreated != null)
            return false;
        if (dateClientsCome != null ? !dateClientsCome.equals(ordering.dateClientsCome) : ordering.dateClientsCome != null)
            return false;
        if (description != null ? !description.equals(ordering.description) : ordering.description != null)
            return false;
        if (whoTakenOrder != null ? !whoTakenOrder.equals(ordering.whoTakenOrder) : ordering.whoTakenOrder != null)
            return false;
        if (whoServesOrder != null ? !whoServesOrder.equals(ordering.whoServesOrder) : ordering.whoServesOrder != null)
            return false;
        if (fund != null ? !fund.equals(ordering.fund) : ordering.fund != null) return false;
        if (type != ordering.type) return false;
        return !(denominations != null ? !denominations.equals(ordering.denominations) : ordering.denominations != null);

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "OrderingPanel{" +
                "id=" + id +
                ", dateOrderCreated=" + dateOrderCreated +
                ", dateClientsCome=" + dateClientsCome +
                ", amountOfPeople=" + amountOfPeople +
                ", description='" + description + '\'' +
                ", whoTakenOrder=" + whoTakenOrder.getName() +
                ", whoServesOrder=" + whoServesOrder +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Ordering ordering = (Ordering) o;
        return this.getDateOrderCreated().compareTo(ordering.getDateOrderCreated());
    }

    public transferFiles.model.order.Ordering toTransferOrdering() {
        transferFiles.model.order.Ordering orderingTO = createOrderingTO();
        if (this.getFund() != null) orderingTO.setFund(this.getFund().toTransferFund(orderingTO));
        List<transferFiles.model.denomination.Denomination> denomsTO = createDenominationsTO(orderingTO);
        orderingTO.setDenominations(denomsTO);
        return orderingTO;
    }

    private List<transferFiles.model.denomination.Denomination> createDenominationsTO(transferFiles.model.order.Ordering orderingTO) {
        List<transferFiles.model.denomination.Denomination> denomsTO = new LinkedList<>();
        for (Denomination denomination : denominations) {
            denomsTO.add(denomination.toDenominationTO(orderingTO));
        }
        return denomsTO;
    }

    private transferFiles.model.order.Ordering createOrderingTO() {
        return new transferFiles.model.order.Ordering(this.getId(), ConvertDate.toJoda(this.getDateOrderCreated()),
                ConvertDate.toJoda(this.getDateClientsCome()), this.getAmountOfPeople(),
                this.getDescription(), this.getAdvancePayment(),
                this.whoTakenOrder != null ? this.getWhoTakenOrder().toTransferUser() : null,
                this.whoServesOrder != null ? this.getWhoServesOrder().toTransferUser() : null,
                this.getType());
    }

    public transferFiles.model.order.Ordering toTransferOrdering(transferFiles.model.denomination.Denomination denominationTO) {
        transferFiles.model.order.Ordering orderingTO = createOrderingTO();
        orderingTO.setFund(this.fund.toTransferFund(orderingTO));
        List<transferFiles.model.denomination.Denomination> denomsTO = new LinkedList<>();
        try {
            for (Denomination denomination : denominations) {
                if (denomination.getId() == denominationTO.getId()) {
                    denomsTO.add(denominationTO);
                } else {
                    denomsTO.add(denomination.toDenominationTO(orderingTO));
                }
            }
        } catch (LazyInitializationException e) {
            denomsTO = null;
        }
        orderingTO.setDenominations(denomsTO);
        return orderingTO;
    }

    public transferFiles.model.order.Ordering toTransferOrdering(transferFiles.model.fund.Fund fundTo) {
        transferFiles.model.order.Ordering orderingTO = createOrderingTO();
        orderingTO.setFund(fundTo);
        List<transferFiles.model.denomination.Denomination> denomsTO = createDenominationsTO(orderingTO);
        orderingTO.setDenominations(denomsTO);
        return orderingTO;
    }


    public static Ordering toPersistentOrdering(transferFiles.model.order.Ordering orderingTO) {
        Ordering ordering = createPersistentOrderingFromTO(orderingTO);
        ordering.setFund(orderingTO.getFund() != null ? Fund.toPersistentFund(orderingTO.getFund(), ordering) : null);
        List<Denomination> orderDenoms = new LinkedList<>();
        if (orderingTO.getDenominations() != null) {
            for (transferFiles.model.denomination.Denomination orderingTOdenom : orderingTO.getDenominations()) {
                orderDenoms.add(Denomination.toPersistentDenomination(orderingTOdenom));
            }
        }
        ordering.setDenominations(orderDenoms);
        return ordering;
    }


    public static Ordering toPersistentOrdering(transferFiles.model.order.Ordering orderingTO, Denomination denomination) {
        Ordering ordering = createPersistentOrderingFromTO(orderingTO);
        ordering.setFund(Fund.toPersistentFund(orderingTO.getFund(), ordering));
        List<Denomination> orderDenoms = new LinkedList<>();
        for (transferFiles.model.denomination.Denomination orderingTOdenom : orderingTO.getDenominations()) {
            if ((denomination != null) && (orderingTOdenom.getId() == denomination.getId())) {
                orderDenoms.add(denomination);
            } else {
                orderDenoms.add(Denomination.toPersistentDenomination(orderingTOdenom, ordering));
            }
        }
        ordering.setDenominations(orderDenoms);
        return ordering;
    }

    public static Ordering toPersistentOrdering(transferFiles.model.order.Ordering orderingTO, Fund fund) {
        Ordering ordering = createPersistentOrderingFromTO(orderingTO);
        ordering.setFund(fund);
        List<Denomination> orderDenoms = new LinkedList<>();
        for (transferFiles.model.denomination.Denomination orderingTOdenom : orderingTO.getDenominations()) {
            orderDenoms.add(Denomination.toPersistentDenomination(orderingTOdenom, ordering));
        }
        ordering.setDenominations(orderDenoms);
        return ordering;
    }

    public static List<transferFiles.model.order.Ordering> getTransferOrderingList(List<Ordering> persistentPrderingList) {
        List<transferFiles.model.order.Ordering> transferOrderingsList = new LinkedList<>();
        for (Ordering ordering : persistentPrderingList) {
            transferOrderingsList.add(ordering.toTransferOrdering());
        }
        return transferOrderingsList;
    }

    private static Ordering createPersistentOrderingFromTO(transferFiles.model.order.Ordering orderingTO) {
        return new Ordering(orderingTO.getId(),
                ConvertDate.toJavaFromJoda(orderingTO.getDateOrderCreated()),
                ConvertDate.toJavaFromJoda(orderingTO.getDateClientsCome()),
                orderingTO.getAmountOfPeople(),
                orderingTO.getDescription(),
                orderingTO.getAdvancePayment(),
                orderingTO.getWhoTakenOrder() != null ? User.toPersistentUser(orderingTO.getWhoTakenOrder()) : null,
                orderingTO.getWhoServesOrder() != null ? User.toPersistentUser(orderingTO.getWhoServesOrder()) : null,
                orderingTO.getType()
        );
    }
}
