package co.com.app.usecase.usecasetokenizacion;

import co.com.app.model.models.dto.request.*;
import co.com.app.model.models.dto.request.Data;
import co.com.app.model.models.dto.request.Pagination;
import co.com.app.model.models.dto.response.*;
import co.com.app.model.models.enums.*;
import co.com.app.model.models.utils.StringUtils;
import co.com.app.model.modeltokenizacion.ActionHistory;
import co.com.app.model.modeltokenizacion.gateways.ActionHistoryRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static co.com.app.model.models.utils.StringUtils.*;

@RequiredArgsConstructor
public class UsecaseTokenizacionUseCase {
    private final ActionHistoryRepository actionHistoryRepository;
    private static final Logger log = Logger.getLogger(UsecaseTokenizacionUseCase.class.getName());

    public ResponseActionHistory doAction(Data request, String messageId) {
        validatePagination(request.getPagination());
        co.com.app.model.models.dto.request.Pagination pagination = request.getPagination();
        Identification identification = request.getCustomer().getIdentification();
        String identificationType = IdentificationTypeEnum.code(identification.getType());

        SearchDate searchDate = request.getActionHistoryRequest().getSearchDate();
        Timestamp dateFrom = convertIsoToTimestamp(searchDate.getDateFrom());
        Timestamp dateTo = convertIsoToTimestamp(searchDate.getDateTo());
        List<SearchAction> searchActionList = getSearchActionList(request.getActionHistoryRequest());
        String stateTokens = transformSearchActionsToString(searchActionList, "type");
        String levelOfTrust = transformSearchActionsToString(searchActionList, "state");

        SearchTokenization searchTokenization = getSearchTokenization(request.getActionHistoryRequest());
        String franchise = getFranchise(searchTokenization);
        List<SearchCustomerCard> customerCardList = getCustomerCardList(searchTokenization);
        String typeCustomerCard = transformCustomerCardTypeToString(customerCardList);
        String binCards = transformCustomerCardToBinOrEndFpan(customerCardList, "bin");
        String endCards = transformCustomerCardToBinOrEndFpan(customerCardList, "endCards");

        String tokenizationState = getTokenizationState(searchTokenization);
        List<SearchDevice> deviceList = getDeviceList(searchTokenization);
        String devicesType = listDevicesTolistType(deviceList);
        String devicesSeid = listDevicesTolistSeid(deviceList);
        List<SearchBusiness> businessList = getBusinessList(searchTokenization);
        String businessId = listBusinessTolistBusinessId(businessList);

        log.info(identification.getNumber());
        Integer total = actionHistoryRepository.totalActionHistory(identification.getNumber(), identificationType, dateFrom,
                dateTo, stateTokens, levelOfTrust, franchise, tokenizationState, typeCustomerCard, binCards, endCards,
                businessId, devicesType, devicesSeid);
        log.log(Level.INFO, "Total action history: {0}", total);
        int pageSize = toInt(pagination.getPageSize(), "pagination.pageSize");
        int pageNumber = toInt(pagination.getPageNumber(), "pagination.pageNumber");
        boolean moreRegisters = total > pageSize * pageNumber;

        if (total > 0) {
            validatePagination(request.getPagination(), total);
        }

        List<ActionHistory> listActionHistory = actionHistoryRepository.getActionHistory(identification.getNumber(),
                identificationType, dateFrom, dateTo, stateTokens, levelOfTrust, franchise, tokenizationState,
                typeCustomerCard, binCards, endCards, businessId, devicesType, devicesSeid, pagination);

        validateActionHistory(listActionHistory);

        List<ActionHistoryResponse> actionHistoryResponses = new ArrayList<>();
        listActionHistory.forEach(actionHistory -> {
            Action action = mapToAction(actionHistory);
            Tokenization tokenization = mapToTokenization(actionHistory);
            actionHistoryResponses.add(ActionHistoryResponse.builder()
                    .action(action)
                    .tokenization(tokenization)
                    .build());
        });

        return ResponseActionHistory.builder()
                .data(co.com.app.model.models.dto.response.Data.builder()
                        .actionHistoryResponse(actionHistoryResponses)
                        .build())
                .meta(Meta.builder()
                        ._applicationId(messageId)
                        ._requestDateTime(StringUtils.convertTimestampToIso(Timestamp.valueOf(LocalDateTime.now())))
                        ._messageId(messageId)
                        .paginationResponse(co.com.app.model.models.dto.response.Pagination.builder()
                                .moreRegisters(moreRegisters)
                                .totalRecords(total)
                                .build())
                        .build())
                .build();
    }

    public Action mapToAction(ActionHistory actionHistory){
        String[] declineReasons = StringUtils.splitDeclineReason(actionHistory.getDeclineReason());
        if(Boolean.FALSE.equals(actionHistory.getIsConfirmed()) &&
                Objects.equals(actionHistory.getLevelOfTrust(), "REQUIRE_ADDITIONAL_AUTHENTICATION")){
            //Identificar REQUIRE_ADDITIONAL_AUTHENTICATION con isConfirmed = false
            actionHistory.setLevelOfTrust(actionHistory.getLevelOfTrust()+"_NCO");
        }
        Action.ActionBuilder actionBuilder = Action.builder();
        if(!isEmpty(actionHistory.getStateToken())){
            actionBuilder.type(replaceNullToEmpty(actionTypeEnum.code(actionHistory.getStateToken())));
        }
        if(!isEmpty(StringUtils.convertTimestampToIso(actionHistory.getDateUpdate()))){
            actionBuilder
                    .actionDate(replaceNullToEmpty(StringUtils.convertTimestampToIso(actionHistory.getDateUpdate())));
        }
        actionBuilder.state(replaceNullToEmpty(actionStateEnum.code(actionHistory.getLevelOfTrust())));
        if (!isEmpty(actionHistory.getOldFpan())) {
            actionBuilder.previousCard(PreviousCard.builder()
                    .customerCard(CustomerCard.builder()
                            .type(replaceNullToEmpty(customerCardEnum.abbreviation(actionHistory.getBinType())))
                            .number(replaceNullToEmpty(maskCardNumber(actionHistory.getOldFpan())))
                            .build()).build());
        }
        if (declineReasons.length > 0 && !isEmpty(replaceNullToEmpty(declineReasons[0]))) {
            actionBuilder.stateCode(replaceNullToEmpty(declineReasons[0]));
        }
        if (declineReasons.length > 1 && !isEmpty(replaceNullToEmpty(declineReasons[1]))) {
            actionBuilder.stateDetail(replaceNullToEmpty(declineReasons[1]));
        }
        return actionBuilder.build();
    }

    public Tokenization mapToTokenization(ActionHistory actionHistory){
        String captureInformationType = replaceNullToEmpty(captureInformationEnum.abbreviation(actionHistory.getCaptureMethod()));
        String deviceType = replaceNullToEmpty(actionHistory.getTypeDevice());
        String deviceName = replaceNullToEmpty(actionHistory.getNameDevice());
        String deviceSeid = replaceNullToEmpty(actionHistory.getSeid());
        String tokenizationState = replaceNullToEmpty(tokenizationStateEnum.code(actionHistory.getHistoryStateToken()));

        Card.CardBuilder cardBuilder = Card.builder();
        if (!isEmpty(StringUtils.matchFranchise(actionHistory.getBin(), false))) {
            cardBuilder.franchise(replaceNullToEmpty(StringUtils.matchFranchise(actionHistory.getBin(), false)));
        }

        CustomerCard.CustomerCardBuilder customerCardBuilder = CustomerCard.builder()
                .number(maskCardNumber(actionHistory.getFpan()));
        if (!isEmpty(customerCardEnum.abbreviation(actionHistory.getBinType()))) {
            customerCardBuilder.type(replaceNullToEmpty(customerCardEnum.abbreviation(actionHistory.getBinType())));
        }
        cardBuilder.customerCard(customerCardBuilder.build());

        Tokenization.TokenizationBuilder builder = Tokenization.builder()
                .card(cardBuilder.build());

        if(!isEmpty(tokenizationState)){
            builder.state(tokenizationState);
        }
        Business.BusinessBuilder businessBuilder = Business.builder();
        if (!isEmpty(actionHistory.getTokenRequestorName())) {
            businessBuilder.name(replaceNullToEmpty(actionHistory.getTokenRequestorName()));
        }
        builder.business(businessBuilder
                .id(replaceNullToEmpty(actionHistory.getWalletProviderId()))
                .build());

        if (!isEmpty(deviceType) || !isEmpty(deviceName) || !isEmpty(deviceSeid) || !isEmpty(captureInformationType)) {
            Device.DeviceBuilder deviceBuilder = Device.builder();

            if (!isEmpty(deviceType)) {
                deviceBuilder.type(deviceType);
            }
            if (!isEmpty(deviceName)) {
                deviceBuilder.name(deviceName);
            }
            if (!isEmpty(deviceSeid)) {
                deviceBuilder.secureElementId(deviceSeid);
            }
            if (!isEmpty(captureInformationType)) {
                deviceBuilder.captureInformation(CaptureInformation.builder()
                        .type(captureInformationType)
                        .build());
            }
            builder.device(deviceBuilder.build());
        }
        return builder.build();
    }
}
