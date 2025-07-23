package com.example.bill;

import static com.example.bill.DBHelper.COLUMN_LOG_ID;
import static com.example.bill.DBHelper.COLUMN_LOG_NAME;
import static com.example.bill.DBHelper.COLUMN_LOG_PASS;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private  Context context;
    private static final int MAX_USER = 1;

    public ProductDataSource(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    //PRODUCTS
    public long insertProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, product.getName());
        values.put(DBHelper.COLUMN_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_QUANTITY, product.getQuantity());

        return database.insert(DBHelper.TABLE_PRODUCTS, null, values);
    }

    public int updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, product.getName());
        values.put(DBHelper.COLUMN_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_QUANTITY, product.getQuantity());

        return database.update(DBHelper.TABLE_PRODUCTS, values,
                DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
    }

    public int deleteProduct(int productId) {

        return database.delete(DBHelper.TABLE_PRODUCTS,
                DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    @SuppressLint("Range")
    private Product cursorToProduct(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID)));
        product.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PRICE)));
        product.setQuantity(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_QUANTITY)));
        return product;
    }

    public boolean doesProduct(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_PRODUCTS, null, DBHelper.COLUMN_NAME + " = ?", new String[]{name}, null, null, null);

        boolean already = cursor.getCount() > 0;
        cursor.close();
        return already;
    }

    //ORDERS
    public long insertOrder(Order order) {
        ContentValues values1 = new ContentValues();
        values1.put(DBHelper.COLUMN_ORDESNAME, order.getNamee());
        values1.put(DBHelper.COLUMN_ORDESPRICE, order.getPricee());
        values1.put(DBHelper.COLUMN_ORDESQUANTITY, order.getQuantityy());
        values1.put(DBHelper.COLUMN_ORDESDATE, order.getDate());

        return database.insert(DBHelper.TABLE_ORDERS, null, values1);
    }

    //passed viewProductDetails
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_PRODUCTS, null, null, null,
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Product product = cursorToProduct(cursor);
                products.add(product);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return products;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Cursor cursor1 = database.query(DBHelper.TABLE_ORDERS, null, null, null,
                null, null, null);

        if (cursor1 != null) {
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                Order order = cursorToOrder(cursor1);
                orders.add(order);
                cursor1.moveToNext();
            }
            cursor1.close();
        }
        return orders;
    }

    @SuppressLint("Range")
    private Order cursorToOrder(Cursor cursor1) {
        Order order = new Order();
        order.setIdd(cursor1.getInt(cursor1.getColumnIndex(DBHelper.COLUMN_ORDESID)));
        order.setNamee(cursor1.getString(cursor1.getColumnIndex(DBHelper.COLUMN_ORDESNAME)));
        order.setPricee(cursor1.getDouble(cursor1.getColumnIndex(DBHelper.COLUMN_ORDESPRICE)));
        order.setQuantityy(cursor1.getDouble(cursor1.getColumnIndex(DBHelper.COLUMN_ORDESQUANTITY)));
        order.setDate(cursor1.getString(cursor1.getColumnIndex(DBHelper.COLUMN_ORDESDATE)));
        return order;
    }

    public double[] getAllPricesArray(String selectedDate){
        database = dbHelper.getReadableDatabase();

        String[] columns = {DBHelper.COLUMN_ORDESPRICE};

        String selection = DBHelper.COLUMN_ORDESDATE + " = ?";
        String[] selectionArgs = {selectedDate};

        Cursor cursor = database.query(DBHelper.TABLE_ORDERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        double[] pricesArray = new double[cursor.getCount()];
        int index = 0;

        if (cursor != null){
            while (cursor.moveToNext()){
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ORDESPRICE));
                pricesArray[index] = price;
                index++;
            }
            cursor.close();
        }
        return pricesArray;
    }


    public long insertAdmin(Admin admin) {

        database = dbHelper.getWritableDatabase();

        String query = "SELECT MAX(" + COLUMN_LOG_ID + ") FROM " + DBHelper.TABLE_ADMIN;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int maxId = cursor.getInt(0);
        cursor.close();

        if (maxId >= MAX_USER) {
            // Registration is closed, show a message to the user
            Toast.makeText(context, "Maximum ID value reached.", Toast.LENGTH_SHORT).show();
            return -1; // Indicate that registration was not successful
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_LOG_NAME, admin.getNName());
        values.put(COLUMN_LOG_PASS, admin.getPPass());
        long newRowId = database.insert(DBHelper.TABLE_ADMIN, null, values);
        database.close();

        return newRowId;
    }

    public List<Admin> getAllLogs() {
        List<Admin> admins = new ArrayList<>();
        Cursor cursor2 = database.query(DBHelper.TABLE_ADMIN, null, null, null,
                null, null, null);

        if (cursor2 != null) {
            cursor2.moveToFirst();
            while (!cursor2.isAfterLast()) {
                Admin admin = cursorToAdmin(cursor2);
                admins.add(admin);
                cursor2.moveToNext();
            }
            cursor2.close();
        }
        return admins;
    }

    @SuppressLint("Range")
    private Admin cursorToAdmin(Cursor cursor2) {
        Admin admin = new Admin();
        admin.setIIdd(cursor2.getInt(cursor2.getColumnIndex(COLUMN_LOG_ID)));
        admin.setNNamee(cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_LOG_NAME)));
        admin.setPPass(cursor2.getString(cursor2.getColumnIndex(DBHelper.COLUMN_LOG_PASS)));
        return admin;
    }


    public boolean doesAdmin(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_ADMIN, null, DBHelper.COLUMN_LOG_NAME + " = ?", new String[]{name}, null, null, null);

        boolean already = cursor.getCount() > 0;
        cursor.close();
        return already;
    }

    public boolean logUser(String username, String password) {

        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_ADMIN, null, "username = ? AND password = ?", new String[]{username, password},
                null, null, null);

        int count = cursor.getCount();
        cursor.close();
        database.close();

        return count > 0;
    }

    public boolean doesDate(String selectedDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_ORDERS, null, DBHelper.COLUMN_ORDESDATE + " = ?", new String[]{selectedDate}, null, null, null);

        boolean already = cursor.getCount() > 0;
        cursor.close();
        return already;
    }


    public void backUp(String outFileNAme) {
        final String inFileName = context.getDatabasePath("products.db").toString();

        try{
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            OutputStream outputStream = new FileOutputStream(outFileNAme);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            fis.close();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to Backup Data ... Retry!!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}