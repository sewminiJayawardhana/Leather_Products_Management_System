package lk.ijse.Shop.model;


import lk.ijse.Shop.db.DbConnection;
import lk.ijse.Shop.dto.ProductsDto;
import lk.ijse.Shop.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsModel {

    public boolean deleteProducts(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM products WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        return pstm.executeUpdate() > 0;
    }

    public List<ProductsDto> loadAllProducts() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM products";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ProductsDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new ProductsDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public boolean saveProducts(ProductsDto productsDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO products VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, productsDto.getId());
        pstm.setString(2, productsDto.getDescription());
        pstm.setDouble(3, productsDto.getUnitprice());
        pstm.setInt(4, productsDto.getQtyonhand());

        return pstm.executeUpdate() > 0;
    }



    public boolean updateProducts(ProductsDto productsDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE products SET description = ?, unit_price = ?, qty_on_hand = ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, productsDto.getDescription());
        pstm.setDouble(2, productsDto.getUnitprice());
        pstm.setInt(3, productsDto.getQtyonhand());
        pstm.setString(4, productsDto.getId());

        return pstm.executeUpdate() > 0;
    }
   /* public boolean updateProducts(List<CartTm> tmList) throws SQLException {
        for (CartTm cartTm : tmList) {
            if(!updateQty(cartTm)) {
                return false;
            }
        }
        return true;
    }
*/
    public ProductsDto searchProducts(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM products WHERE code = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();

        ProductsDto dto = null;

        if(resultSet.next()) {
            dto = new ProductsDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return dto;
    }
    private boolean updateQty(CartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE products SET qty_on_hand = qty_on_hand - ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, cartTm.getQty());
        pstm.setString(2, cartTm.getCode());

        return pstm.executeUpdate() > 0; //true
    }

}




