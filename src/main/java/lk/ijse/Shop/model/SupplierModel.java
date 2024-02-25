package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.SupplierDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public boolean saveSupplier(final SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO supplier VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSupplierId());
        pstm.setString(2, dto.getSupplierName());
        pstm.setString(3, dto.getAddress());
        pstm.setString(4, dto.getTel());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }


    public boolean updateSupplier(final SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE supplier SET supplier_name = ?, address = ?, tel = ? WHERE supplier_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getSupplierName());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getTel());
        pstm.setString(4, dto.getSupplierId());

        return pstm.executeUpdate() > 0;
    }

    public SupplierDto searchSupplier(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier WHERE supplier_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        SupplierDto dto = null;

        if(resultSet.next()) {
            String sup_id = resultSet.getString(1);
            String sup_name = resultSet.getString(2);
            String sup_address = resultSet.getString(3);
            String sup_tel = resultSet.getString(4);

            dto = new SupplierDto(sup_id, sup_name, sup_address, sup_tel);
        }

        return dto;
    }

    public List<SupplierDto> getAllSuppliers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<SupplierDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String sup_id = resultSet.getString(1);
            String sup_name = resultSet.getString(2);
            String sup_address = resultSet.getString(3);
            String sup_tel = resultSet.getString(4);

            var dto = new SupplierDto(sup_id, sup_name, sup_address, sup_tel);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean deleteSupplier(String supplierId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM supplier WHERE supplier_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, supplierId);

        return pstm.executeUpdate() > 0;
    }

    public List<SupplierDto> loadAllSuppliers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<SupplierDto> supList = new ArrayList<>();

        while (resultSet.next()) {
            supList.add(new SupplierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            ));
        }
        return supList;
    }
}