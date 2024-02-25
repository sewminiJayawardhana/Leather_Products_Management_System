package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.RawMaterialDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RawmaterialModel {
    public boolean saveRawMaterial(final RawMaterialDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO raw_material VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getRawMaterialId());
        pstm.setDouble(2, dto.getQtyOnStock());
        pstm.setDouble(3, dto.getUnitPrice());


        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateRawMaterial(final RawMaterialDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE raw_material SET qty = ?, raw_unitprice = ? WHERE raw_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);


        pstm.setDouble(1, dto.getQtyOnStock());
        pstm.setDouble(2, dto.getUnitPrice());
        pstm.setString(3, dto.getRawMaterialId());


        return pstm.executeUpdate() > 0;
    }

    public RawMaterialDto searchRawMaterial(String rawMaterialId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM raw_material WHERE raw_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, rawMaterialId);

        ResultSet resultSet = pstm.executeQuery();

        RawMaterialDto dto = null;

        if (resultSet.next()) {
            String raw_id = resultSet.getString(1);
            Double qty = resultSet.getDouble(2);
            Double unit_price = resultSet.getDouble(3);


            dto = new RawMaterialDto(raw_id, qty, unit_price);
        }

        return dto;
    }

    public List<RawMaterialDto> getAllMaterials() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM raw_material";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<RawMaterialDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String raw_id = resultSet.getString(1);
            Double qty = resultSet.getDouble(2);
            Double unit_price = resultSet.getDouble(3);


            var dto = new RawMaterialDto(raw_id, qty, unit_price);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean deleteRawMaterial(String rawMaterialId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM raw_material WHERE raw_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, rawMaterialId);

        return pstm.executeUpdate() > 0;
    }

    public static List<RawMaterialDto> loadAllMaterials() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM raw_material";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<RawMaterialDto> rawList = new ArrayList<>();

        while (resultSet.next()) {
            rawList.add(new RawMaterialDto(
                    resultSet.getString(1),
                    resultSet.getDouble(2),
                    resultSet.getDouble(3)

            ));
        }
        return rawList;
    }
}