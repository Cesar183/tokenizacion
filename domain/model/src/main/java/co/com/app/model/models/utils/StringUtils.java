package co.com.app.model.models.utils;

import co.com.app.model.models.constants.CardConstants;
import co.com.app.model.models.dto.request.*;
import co.com.app.model.models.enums.actionStateEnum;
import co.com.app.model.models.enums.customerCardEnum;
import co.com.app.model.models.enums.franchiseEnum;
import co.com.app.model.models.enums.tokenizationStateEnum;
import co.com.app.model.models.exception.BP404005Exception;
import co.com.app.model.models.exception.BP404006Exception;
import co.com.app.model.models.exception.SA400Exception;
import co.com.app.model.modeltokenizacion.ActionHistory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringUtils {
    private static String result;
    private static final String GUION = " - ";
    private static final String MASKED_SECTION = "X";
    private static final int FROM = 2;
    private static final int TO = 4;
    private static final int TOTAL_DIGITS = 10;
    private static final int FIRST_SIX_DIGITS = 6;
    private static final int LAST_FOUR_DIGITS = 4;
    private static final int NUMBER_LENGTH_FOUR = 4;
    private static final int NUMBER_LENGTH_SIX = 6;
    private static final int PAGE_SIZE_MAX = 10;
    private static final int HOURS_DIFFERENCE = 5;

    private StringUtils () {
    }

    public static String matchFranchise(String bin, boolean isDebit) {
        if(bin == null){
            return null;
        }
        bin = bin.trim();
        result = bin;
        if (isDebit) {
            evalDebit(bin);
        } else {
            evalCredit(bin);
        }
        return result;
    }

    public static String dataFormat(String date, String cardType) {
        String[] dataList = date.split("/");

        if("DEBIT".equals(cardType)){
            return dataList[1] + dataList[0];
        }else{
            return dataList[1].substring(FROM, TO)  + dataList[0];
        }
    }

    private static void evalDebit(String bin) {
        if (bin.contains("601660")) {
            result = CardConstants.CARD_DEBIT_MASTER;
        } else if (bin.contains("530691")) {
            result = CardConstants.CARD_DEBIT_MASTER_DEBIT;
        }
    }

    private static void evalCredit(String bin) {
        String firstCharacterBin = Character.toString(bin.charAt(0));
        switch (firstCharacterBin) {
            case "4":
                result = franchiseEnum.code("4");
                break;
            case "5":
                result = franchiseEnum.code("5");
                break;
            case "3":
                result = franchiseEnum.code("3");
                break;
            default:
                throw new IllegalArgumentException("No corresponde a ninguna franquicia conocida.");
        }
    }

    public static Timestamp convertIsoToTimestamp(String dateTime) {
        Instant instant = Instant.parse(dateTime).plus(HOURS_DIFFERENCE, ChronoUnit.HOURS);
        return Timestamp.from(instant.atZone(ZoneId.of("America/Bogota")).toInstant());
    }

    public static String convertTimestampToIso(Timestamp timestamp) {
        if(timestamp == null){
            return "";
        }
        Instant instant = timestamp.toInstant();
        var adjustedInstant = instant.minus(HOURS_DIFFERENCE, ChronoUnit.HOURS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(adjustedInstant);
    }

    public static String[] splitDeclineReason(String string) {
        if(string == null || string.isEmpty()){
            return new String[]{"",""};
        }
        return string.split(GUION);
    }

    public static String transformSearchActionsToString(List<SearchAction> searchActions, String field) {
        if(searchActions == null){
            return null;
        }
        String result = String.join(",", searchActions.stream()
                .map(searchAction -> {
                    if ("state".equals(field)) {
                        return actionStateEnum.description(searchAction.getState());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet()));

        if(result.isEmpty()){
            return null;
        }

        return "{" + result + "}";
    }

    public static String transformCustomerCardTypeToString(List<SearchCustomerCard> customerCards) {
        if(customerCards == null){
            return null;
        }
        String result = String.join(",", customerCards.stream()
                .map(customerCard -> customerCardEnum.code(customerCard.getType()))
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet()));

        if(result.isEmpty()){
            return null;
        }

        return "{" + result + "}";
    }

    public static String maskCardNumber(String cardNumber) {
        String result = null;
        if(cardNumber != null) {
            int totalDigits = cardNumber.length();
            if (totalDigits <= TOTAL_DIGITS) {
                return cardNumber;
            }
            String firstFiveDigits = cardNumber.substring(0, FIRST_SIX_DIGITS);
            String lastFourDigits = cardNumber.substring(totalDigits - LAST_FOUR_DIGITS);
            String maskedSection = cardNumber.substring(
                    FIRST_SIX_DIGITS, totalDigits - LAST_FOUR_DIGITS).replaceAll("\\d", MASKED_SECTION);
            result = firstFiveDigits + maskedSection + lastFourDigits;
        }
        return result;
    }

    public static String transformCustomerCardToBinOrEndFpan(List<SearchCustomerCard> customerCards, String field) {
        String result;
        if(customerCards == null){
            return null;
        }
        if (field.equals("bin")) {
            result = getString(customerCards);
        } else {
            result = getResult(customerCards);
        }
        if (result == null || result.isEmpty()) {
            return null;
        }
        return "{" + result + "}";
    }

    private static String getResult(List<SearchCustomerCard> customerCards) {
        return customerCards.stream()
                .map(customerCard -> {
                    String number = customerCard.getNumber();
                    if (number == null) {
                        return null;
                    }
                    if (number.length() < NUMBER_LENGTH_FOUR) {
                        throw new SA400Exception("La longitud del parámetro 'searchCustomerCard.number' es invalida.");
                    }
                    return number.substring(number.length() - NUMBER_LENGTH_FOUR);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }

    private static String getString(List<SearchCustomerCard> customerCards) {
        return customerCards.stream()
                .map(customerCard -> {
                    String number = customerCard.getNumber();
                    if (number == null) {
                        return null;
                    }
                    if (number.length() < NUMBER_LENGTH_SIX) {
                        throw new SA400Exception("La longitud del parámetro 'searchCustomerCard.number' es invalida.");
                    }
                    return number.substring(0, NUMBER_LENGTH_SIX);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.joining(","));
    }

    public static String listDevicesTolistType(List<SearchDevice> devices) {
        if (devices == null) {
            return null;
        }
        String result = String.join(",", devices.stream()
                .map(SearchDevice::getType)
                .filter(Objects::nonNull)
                .peek(type -> {
                    if (type.isEmpty()) {
                        throw new SA400Exception("La longitud del parámetro 'SearchDevice.type' es invalida.");
                    }
                })
                .collect(Collectors.toSet()));
        if(result.isEmpty()){
            return null;
        }
        return "{" + result + "}";
    }

    public static String listDevicesTolistSeid(List<SearchDevice> devices) {
        if (devices == null) {
            return null;
        }
        String result = String.join(",", devices.stream()
                .map(SearchDevice::getSecureElementId)
                .filter(Objects::nonNull)
                .peek(seid -> {
                    if (seid.isEmpty()) {
                        throw new SA400Exception("La longitud del parámetro 'SearchDevice.secureElementId' es invalida.");
                    }
                })
                .collect(Collectors.toSet()));

        if(result.isEmpty()){
            return null;
        }
        return "{" + result + "}";
    }

    public static String replaceNullToEmpty(String imput){
        return imput == null ? "" : imput;
    }
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public static String listBusinessTolistBusinessId(List<SearchBusiness> business) {
        if (business == null) {
            return null;
        }
        String result = business.stream()
                .map(searchBusiness -> {
                    String id = searchBusiness.getId();
                    if (id == null) {
                        throw new SA400Exception("El parámetro 'searchBusiness.id' es requerido..");
                    }
                    if (id.isEmpty()) {
                        throw new SA400Exception("La longitud del parámetro 'searchBusiness.id' es invalida.");
                    }
                    return id;
                })
                .collect(Collectors.joining(","));
        if (result.isEmpty()) {
            return null;
        }
        return "{" + result + "}";
    }
    public static void validatePagination(Pagination pagination) {
        if (pagination == null) {
            throw new SA400Exception("El parámetro 'pagination' es requerido.");
        }
        if (pagination.getPageNumber() == null) {
            throw new SA400Exception("El parámetro 'pagination.pageNumber' es requerido.");
        }
        if (pagination.getPageSize() == null) {
            throw new SA400Exception("El parámetro 'pagination.pageSize' es requerido.");
        }
        int pageSize = toInt(pagination.getPageSize(), "pagination.pageSize");
        if (pageSize > PAGE_SIZE_MAX || pageSize < 1) {
            throw new SA400Exception("El valor del parámetro 'pagination.pageSize' no hace parte de los valores válidos.");
        }
    }
    public static void validatePagination(Pagination pagination, int totalRecords) {
        int pageSize = toInt(pagination.getPageSize(), "pagination.pageSize");
        int pageNumber = toInt(pagination.getPageNumber(), "pagination.pageNumber");
        if (pageNumber > Math.ceil((double) totalRecords / pageSize)) {
            throw new BP404005Exception("No existen más registros con las condiciones de búsqueda.");
        }
    }
    public static void validateActionHistory(List<ActionHistory> listActionHistory) {
        if (listActionHistory.isEmpty()) {
            throw new BP404006Exception("No se encontraron registros que cumplan con los criterios de búsqueda.");
        }
    }
    public static int toInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new SA400Exception("El parámetro '" + fieldName + "' no hace parte de los valores válidos.");
        }
    }
    public static List<SearchAction> getSearchActionList(ActionHistoryRequest actionHistoryRequest) {
        return actionHistoryRequest != null ? actionHistoryRequest.getSearchAction() : Collections.emptyList();
    }

    public static SearchTokenization getSearchTokenization(ActionHistoryRequest actionHistoryRequest) {
        return actionHistoryRequest != null ? actionHistoryRequest.getSearchTokenization() : null;
    }

    public static String getFranchise(SearchTokenization searchTokenization) {
        SearchCard searchCard = searchTokenization != null ? searchTokenization.getSearchCard() : null;
        return searchCard != null ? franchiseEnum.digit(searchCard.getFranchise()) : null;
    }

    public static List<SearchCustomerCard> getCustomerCardList(SearchTokenization searchTokenization) {
        SearchCard searchCard = searchTokenization != null ? searchTokenization.getSearchCard() : null;
        return searchCard != null ? searchCard.getSearchCustomerCard() : null;
    }

    public static String getTokenizationState(SearchTokenization searchTokenization) {
        return searchTokenization != null ? tokenizationStateEnum.description(searchTokenization.getState()) : null;
    }

    public static List<SearchDevice> getDeviceList(SearchTokenization searchTokenization) {
        return searchTokenization != null ? searchTokenization.getSearchDevice() : null;
    }

    public static List<SearchBusiness> getBusinessList(SearchTokenization searchTokenization) {
        return searchTokenization != null ? searchTokenization.getSearchBusiness() : null;
    }
}
