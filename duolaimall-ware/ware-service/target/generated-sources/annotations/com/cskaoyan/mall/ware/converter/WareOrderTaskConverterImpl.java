package com.cskaoyan.mall.ware.converter;

import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDetailDTO;
import com.cskaoyan.mall.ware.model.WareOrderTask;
import com.cskaoyan.mall.ware.model.WareOrderTaskDetail;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-21T15:35:02+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class WareOrderTaskConverterImpl implements WareOrderTaskConverter {

    @Override
    public WareOrderTask convertOrderInfoDTO(OrderInfoDTO orderInfoDTO) {
        if ( orderInfoDTO == null ) {
            return null;
        }

        WareOrderTask wareOrderTask = new WareOrderTask();

        if ( orderInfoDTO.getId() != null ) {
            wareOrderTask.setOrderId( String.valueOf( orderInfoDTO.getId() ) );
        }
        wareOrderTask.setOrderBody( orderInfoDTO.getTradeBody() );
        wareOrderTask.setDetails( orderDetailDTOListToWareOrderTaskDetailList( orderInfoDTO.getOrderDetailList() ) );
        wareOrderTask.setId( orderInfoDTO.getId() );
        wareOrderTask.setCreateTime( orderInfoDTO.getCreateTime() );
        wareOrderTask.setUpdateTime( orderInfoDTO.getUpdateTime() );
        wareOrderTask.setConsignee( orderInfoDTO.getConsignee() );
        wareOrderTask.setConsigneeTel( orderInfoDTO.getConsigneeTel() );
        wareOrderTask.setDeliveryAddress( orderInfoDTO.getDeliveryAddress() );
        wareOrderTask.setOrderComment( orderInfoDTO.getOrderComment() );
        wareOrderTask.setPaymentWay( orderInfoDTO.getPaymentWay() );
        wareOrderTask.setTrackingNo( orderInfoDTO.getTrackingNo() );
        wareOrderTask.setWareId( orderInfoDTO.getWareId() );

        return wareOrderTask;
    }

    @Override
    public WareOrderTaskDetail convertOrderDetailDTO(OrderDetailDTO orderDetailDTO) {
        if ( orderDetailDTO == null ) {
            return null;
        }

        WareOrderTaskDetail wareOrderTaskDetail = new WareOrderTaskDetail();

        if ( orderDetailDTO.getSkuId() != null ) {
            wareOrderTaskDetail.setSkuId( String.valueOf( orderDetailDTO.getSkuId() ) );
        }
        wareOrderTaskDetail.setSkuName( orderDetailDTO.getSkuName() );
        wareOrderTaskDetail.setSkuNum( orderDetailDTO.getSkuNum() );

        return wareOrderTaskDetail;
    }

    @Override
    public WareOrderTask converWareOrderTask(WareOrderTaskDTO wareOrderTaskDTO) {
        if ( wareOrderTaskDTO == null ) {
            return null;
        }

        WareOrderTask wareOrderTask = new WareOrderTask();

        wareOrderTask.setOrderId( wareOrderTaskDTO.getOrderId() );
        wareOrderTask.setConsignee( wareOrderTaskDTO.getConsignee() );
        wareOrderTask.setConsigneeTel( wareOrderTaskDTO.getConsigneeTel() );
        wareOrderTask.setDeliveryAddress( wareOrderTaskDTO.getDeliveryAddress() );
        wareOrderTask.setOrderComment( wareOrderTaskDTO.getOrderComment() );
        wareOrderTask.setPaymentWay( wareOrderTaskDTO.getPaymentWay() );
        wareOrderTask.setTaskStatus( wareOrderTaskDTO.getTaskStatus() );
        wareOrderTask.setOrderBody( wareOrderTaskDTO.getOrderBody() );
        wareOrderTask.setTrackingNo( wareOrderTaskDTO.getTrackingNo() );
        wareOrderTask.setWareId( wareOrderTaskDTO.getWareId() );
        wareOrderTask.setTaskComment( wareOrderTaskDTO.getTaskComment() );
        wareOrderTask.setDetails( wareOrderTaskDetailDTOListToWareOrderTaskDetailList( wareOrderTaskDTO.getDetails() ) );

        return wareOrderTask;
    }

    @Override
    public WareOrderTaskDetail convertOrderTaskDetailDTO(WareOrderTaskDetailDTO wareOrderTaskDetailDTO) {
        if ( wareOrderTaskDetailDTO == null ) {
            return null;
        }

        WareOrderTaskDetail wareOrderTaskDetail = new WareOrderTaskDetail();

        wareOrderTaskDetail.setSkuId( wareOrderTaskDetailDTO.getSkuId() );
        wareOrderTaskDetail.setSkuName( wareOrderTaskDetailDTO.getSkuName() );
        wareOrderTaskDetail.setSkuNum( wareOrderTaskDetailDTO.getSkuNum() );
        wareOrderTaskDetail.setTaskId( wareOrderTaskDetailDTO.getTaskId() );

        return wareOrderTaskDetail;
    }

    @Override
    public List<WareOrderTask> convertWareOrderTaskDTO(List<WareOrderTaskDTO> wareOrderTaskDTOS) {
        if ( wareOrderTaskDTOS == null ) {
            return null;
        }

        List<WareOrderTask> list = new ArrayList<WareOrderTask>( wareOrderTaskDTOS.size() );
        for ( WareOrderTaskDTO wareOrderTaskDTO : wareOrderTaskDTOS ) {
            list.add( converWareOrderTask( wareOrderTaskDTO ) );
        }

        return list;
    }

    protected List<WareOrderTaskDetail> orderDetailDTOListToWareOrderTaskDetailList(List<OrderDetailDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<WareOrderTaskDetail> list1 = new ArrayList<WareOrderTaskDetail>( list.size() );
        for ( OrderDetailDTO orderDetailDTO : list ) {
            list1.add( convertOrderDetailDTO( orderDetailDTO ) );
        }

        return list1;
    }

    protected List<WareOrderTaskDetail> wareOrderTaskDetailDTOListToWareOrderTaskDetailList(List<WareOrderTaskDetailDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<WareOrderTaskDetail> list1 = new ArrayList<WareOrderTaskDetail>( list.size() );
        for ( WareOrderTaskDetailDTO wareOrderTaskDetailDTO : list ) {
            list1.add( convertOrderTaskDetailDTO( wareOrderTaskDetailDTO ) );
        }

        return list1;
    }
}
