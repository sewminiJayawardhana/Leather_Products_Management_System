package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.PlaceOrderDto;
import lk.ijse.Shop.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderModel {
    private final OrderModel orderModel = new OrderModel();
    private final OrderDetailModel orderDetailModel = new OrderDetailModel();

    public boolean placeOrder(PlaceOrderDto pDto) throws SQLException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = OrderModel.saveOrder(pDto.getOrderId(), pDto.getCusId(), pDto.getDate());
            if (isOrderSaved) {
                boolean isOrderDetailSaved = orderDetailModel.saveOrderDetail(pDto.getOrderId(), pDto.getTmList());
                if (isOrderDetailSaved) {
                    connection.commit();
                    result = true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);

            }
        }
        return result;
    }
}
