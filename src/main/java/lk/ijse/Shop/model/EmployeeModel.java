package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public List<EmployeeDto> getAllEmployees() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<EmployeeDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_name = resultSet.getString(2);
            String intime = resultSet.getString(3);
            String leavetime = resultSet.getString(4);

            var dto = new EmployeeDto(emp_id, emp_name, intime, leavetime);
            dtoList.add(dto);
        }
        return dtoList;
    }



    public boolean deleteEmployee(String id) throws SQLException {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "DELETE FROM employee WHERE emp_id = ?";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, id);

            return pstm.executeUpdate() > 0;

        }

    public boolean saveEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getIntime());
        pstm.setString(4, dto.getLeavetime());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employee SET emp_name = ?, in_time = ?, leave_time = ? WHERE emp_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getIntime());
        pstm.setString(3, dto.getLeavetime());
        pstm.setString(4, dto.getId());

        return pstm.executeUpdate() > 0;
    }

    public EmployeeDto searchEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE emp_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        EmployeeDto dto = null;

        if(resultSet.next()) {
            String emp_id = resultSet.getString(1);
            String emp_name = resultSet.getString(2);
            String in_time = resultSet.getString(3);
            String leave_time = resultSet.getString(4);

            dto = new EmployeeDto(emp_id, emp_name, in_time, leave_time);
        }

        return dto;
    }
}

