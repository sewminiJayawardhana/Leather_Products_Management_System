package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.SalaryDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryModel {
    public boolean deleteSalary(String s_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM salary WHERE s_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1,s_id);

        return pstm.executeUpdate()>0;
    }

    public boolean saveSalary(SalaryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO salary VALUES(?,?,?,?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getId());
        pstm.setString(2,dto.getE_id());
        pstm.setString(3,dto.getMonth());
        pstm.setString(4, String.valueOf(dto.getSalary()));

        boolean isSaved = pstm.executeUpdate()>0;
        return isSaved;
    }

    public boolean updateSalary(SalaryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE salary set emp_id = ?,month = ?,amount = ? WHERE s_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1,dto.getE_id());
        pstm.setString(2,dto.getMonth());
        pstm.setString(3, String.valueOf(dto.getSalary()));
        pstm.setString(4, dto.getId());

        return pstm.executeUpdate()>0;
    }


    public SalaryDto searchSalary(String s_id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM salary WHERE s_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, s_id);

        ResultSet resultSet = pstm.executeQuery();

        SalaryDto dto = null;

        if(resultSet.next()) {
            String sal_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String sal_month = resultSet.getString(3);
            double amount = Double.parseDouble(resultSet.getString(4));

            dto = new SalaryDto(sal_id,e_id,sal_month,amount);
        }

        return dto;
    }

    public List<SalaryDto> getAllSalary() throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();

        String sql = "select * from salary";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<SalaryDto> dtoList= new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            String s_id = resultSet.getString(1);
            String e_id = resultSet.getString(2);
            String month = resultSet.getString(3);
            double amount = Double.parseDouble(resultSet.getString(4));

            var dto = new SalaryDto(s_id,e_id,month,amount);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
