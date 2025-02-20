package co.com.app.apirest.validator;

import co.com.app.model.models.dto.request.*;
import co.com.app.model.models.enums.*;
import co.com.app.model.models.exception.BP400003Exception;
import co.com.app.model.models.exception.SA400Exception;
import co.com.app.model.models.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionHistoryRequestValidator {
    private static final int MAX_SEARCH_DAYS = 180;
    private static final int MAX_YEARS_BACK = 5;
    private static final int MAX_LENGTH_IDENTIFICATION = 30;

    public static void validate(ApiRequest request){
        validateCustomer(request.getData().getCustomer());
        Identification identification = request.getData().getCustomer().getIdentification();
        validateIdentification(identification);
        ActionHistoryRequest actionHistoryRequest = request.getData().getActionHistoryRequest();
        validateActionHistoryRequest(actionHistoryRequest);
        SearchDate searchDate = actionHistoryRequest.getSearchDate();
        validateDate(searchDate);
        List<SearchAction> actions = actionHistoryRequest.getSearchAction();
        validateAction(actions);
        SearchTokenization searchTokenization = actionHistoryRequest.getSearchTokenization();
        if (searchTokenization != null) {
            validateTokenization(searchTokenization);
            SearchCard searchCard = searchTokenization.getSearchCard();
            if (searchCard != null) {
                validateCard(searchCard);
                List<SearchCustomerCard> listCustomerCard = searchCard.getSearchCustomerCard();
                validateCustomerCard(listCustomerCard);
            }
        }
    }

    private static void validateCustomer(Customer customer){
        if(customer == null){
            throw new SA400Exception("El parámetro 'customer' es requerido.");
        }
    }
    private static void validateIdentification(Identification identification){
        if (identification == null || StringUtils.isEmpty(identification.getNumber())) {
            throw new SA400Exception("El parámetro 'identification.number' es requerido.");
        } else {
            String number = identification.getNumber();
            if (number.length() > MAX_LENGTH_IDENTIFICATION) {
                throw new SA400Exception("La longitud del parámetro 'identification.number' es invalida.");
            }
            String type = identification.getType();
            if (StringUtils.isEmpty(type)) {
                throw new SA400Exception("El parámetro 'identification.type' es requerido.");
            }
            validateIdentificationType(type);
        }
    }
    private static void validateIdentificationType(String type){
        if(type.isEmpty()){
            throw new SA400Exception("La longitud del parámetro 'identification.type' es invalida.");
        }
        try{
            IdentificationTypeEnum.fromValue(type);
        }catch (IllegalArgumentException e){
            throw new SA400Exception("El valor del parámetro 'identification.type' no hace parte de los valores validos");
        }
    }
    private static void validateActionHistoryRequest(ActionHistoryRequest actionHistoryRequest){
        if(actionHistoryRequest == null){
            throw new SA400Exception("El parámetro 'actionHistoryRequest' es requerido.");
        }
    }
    private static void validateDate(SearchDate searchDate) {
        if (searchDate == null) {
            throw new SA400Exception("El parámetro 'searchDate' es requerido.");
        }

        validateNonNullDate(searchDate.getDateFrom(), "dateFrom");
        validateNonNullDate(searchDate.getDateTo(), "dateTo");

        validateDateFormat(searchDate.getDateFrom(), "dateFrom");
        validateDateFormat(searchDate.getDateTo(), "dateTo");
        validateDateRange(searchDate.getDateFrom(), searchDate.getDateTo());
        validateMaxYearsBack(searchDate.getDateFrom());
    }

    private static void validateNonNullDate(String date, String fieldName) {
        if (date == null) {
            throw new SA400Exception("El parámetro 'searchDate." + fieldName + "' es requerido.");
        }
    }

    private static void validateDateFormat(String date, String dateName){
        if(date != null) {
            String exp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)(.\\d{2}([0-9]))?Z$";
            if (!Pattern.matches(exp, date)){
                throw new SA400Exception("El formato del parámetro 'searchDate."+dateName+"' es invalido");
            }
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                OffsetDateTime.parse(date, formatter);
            } catch (DateTimeParseException e) {
                throw new SA400Exception("Datos de entrada del campo 'searchDate." + dateName + "' no válidos.");
            }
        }
    }

    private static void validateDateRange(String dateFrom, String dateTo) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime from = OffsetDateTime.parse(dateFrom, formatter);
        OffsetDateTime to = OffsetDateTime.parse(dateTo, formatter);
        long daysBetween = ChronoUnit.DAYS.between(from, to);
        if (daysBetween > MAX_SEARCH_DAYS) {
            throw new BP400003Exception("El valor de los parámetros de fecha está en un rango inválido o fuera del histórico");
        }
    }

    private static void validateMaxYearsBack(String dateFrom) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime from = OffsetDateTime.parse(dateFrom, formatter);
        OffsetDateTime maxDateFrom = OffsetDateTime.now().minusYears(MAX_YEARS_BACK);
        if (from.isBefore(maxDateFrom)) {
            throw new BP400003Exception("El valor de los parámetros de fecha está en un rango inválido o fuera del histórico");
        }
    }

    private static void validateAction(List<SearchAction> searchActions) {
        if(searchActions != null){
            searchActions.forEach( action -> {
                String state = action.getState();
                validateActionState(state);
            });
        }
    }

    private static void validateActionState(String state) {
        if(state == null){
            throw new SA400Exception("El parámetro 'searchAction.state' es requerido.");
        }
        if(state.isEmpty()){
            throw new SA400Exception("La longitud del parámetro 'searchAction.state' es invalida.");
        }
        try{
            actionStateEnum.fromValue(state);
        }catch (IllegalArgumentException e){
            throw new SA400Exception("El valor del parámetro 'searchAction.state' no hace parte de los valores validos");
        }
    }

    private static void validateTokenization(SearchTokenization searchTokenization) {
        if (searchTokenization != null) {
            String state = searchTokenization.getState();
            if (state != null) {
                validateTokenizationState(state);
            }
            SearchCard searchCard = searchTokenization.getSearchCard();
            if (searchCard != null) {
                validateCard(searchCard);
            }
        }
    }

    private static void validateTokenizationState(String state) {
        if (state.isEmpty()) {
            throw new SA400Exception("La longitud del parámetro 'searchAction.state' es invalida.");
        }
        try {
            tokenizationStateEnum.fromValue(state);
        }catch (IllegalArgumentException e){
            throw new SA400Exception("El valor del parámetro 'searchTokenization.state' no hace parte de los valores validos");
        }
    }
    private static void validateCard(SearchCard searchCard) {
        if (searchCard != null) {
            String franchiseEnum = searchCard.getFranchise();
            if (franchiseEnum != null) {
                validateFranchiseState(franchiseEnum);
            }
            List<SearchCustomerCard> listCustomerCard = searchCard.getSearchCustomerCard();
            if (listCustomerCard != null) {
                validateCustomerCard(listCustomerCard);
            }
        }
    }

    private static void validateFranchiseState(String franchise) {
        if (franchise != null) {
            if (franchise.isEmpty()) {
                throw new SA400Exception("La longitud del parámetro 'franchise' es inválida.");
            }
            try {
                franchiseEnum.fromValue(franchise);
            } catch (IllegalArgumentException e) {
                throw new SA400Exception("El valor del parámetro 'franchise' no hace parte de los valores válidos");
            }
        }
    }

    private static void validateCustomerCard(List<SearchCustomerCard> customerCard) {
        if (customerCard != null && !customerCard.isEmpty()){
            customerCard.forEach(customerCard1 -> {
                String type = customerCard1.getType();
                validateCustomerCardType(type);
            });
        }
    }

    private static void validateCustomerCardType(String type) {
        if(type != null){
            if (type.isEmpty()) {
                throw new SA400Exception("La longitud del parámetro 'Customer Card Type' es invalida.");
            }
            try {
                customerCardEnum.fromValue(type);
            }catch (IllegalArgumentException e){
                throw new SA400Exception("El valor del parámetro 'Customer Card Type' no hace parte de los valores validos");
            }
        }
    }
}
