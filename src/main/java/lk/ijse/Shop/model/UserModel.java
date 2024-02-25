package lk.ijse.Shop.model;

import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {


    public static boolean saveUser(UserDTO userDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO User VALUES (?,?,?)");
        statement.setObject(3, userDTO.getEmail());
        statement.setObject(1, userDTO.getUsername());
        statement.setObject(2, userDTO.getPassword());

        int i = statement.executeUpdate();
        return 0 < i;

    }

    public static String getEmail(String email) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT email FROM User WHERE email=?");
        statement.setObject(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean isExistUser(String userName, String pw) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT password ,username FROM User WHERE username=? AND password=?");
        statement.setObject(1, userName);
        statement.setObject(2, pw);
        ResultSet resultSet = statement.executeQuery();
        String dbUserName = null;
        String dbPassword = null;
        if (resultSet.next()) {
            dbUserName = resultSet.getString(2);
            dbPassword= resultSet.getString(1);

        }
        return userName.equals(dbUserName) && pw.equals(dbPassword);
    }

    public static UserDTO getUser(String UserName) throws SQLException {
        Connection con = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM User WHERE username = ?");
        pstm.setString(1, UserName);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return  new UserDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)

            );
        }
        return null;
    }
}
