import sqlite3


DATABASE = "store.db"


def get_connection():
    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row
    return conn


# Dashboard Cards

def get_cards():

    conn = get_connection()
    cursor = conn.cursor()

    # Total Products
    cursor.execute("""
        SELECT COUNT(*) AS total_products
        FROM products
    """)
    total_products = cursor.fetchone()["total_products"]

    # Total Available Stock
    cursor.execute("""
        SELECT COALESCE(SUM(quantity), 0) AS total_stock
        FROM products
    """)
    total_stock = cursor.fetchone()["total_stock"]

    # Low Stock Count
    cursor.execute("""
        SELECT COUNT(*) AS low_stock
        FROM products
        WHERE quantity < 10
    """)
    low_stock = cursor.fetchone()["low_stock"]

    conn.close()

    return {
        "total_products": total_products,
        "available_stock": total_stock,
        "low_stock": low_stock
    }


#                                                                                                           
# Sales Chart
#                                                                                                           

def get_sales_chart(from_date, to_date):

    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("""
        SELECT

            p.product_name,

            SUM(bi.quantity) AS sold_quantity

        FROM bill_items bi

        INNER JOIN products p
            ON bi.product_id = p.id

        INNER JOIN bills b
            ON bi.bill_id = b.id

        WHERE DATE(b.created_at)
            BETWEEN ? AND ?

        GROUP BY p.id

        ORDER BY sold_quantity DESC

    """, (from_date, to_date))

    rows = cursor.fetchall()

    conn.close()

    return [dict(row) for row in rows]


# Stock Chart

def get_stock_chart():

    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("""

        SELECT

            product_name,

            quantity

        FROM products

        WHERE quantity > 0

        ORDER BY quantity DESC

    """)

    rows = cursor.fetchall()

    conn.close()

    return [dict(row) for row in rows]


#                                                                                                           
# Low Stock Products
#                                                                                                           

def get_low_stock():

    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("""

        SELECT

            p.product_name,

            c.category_name,

            p.quantity

        FROM products p

        INNER JOIN categories c

            ON p.category_id = c.id

        WHERE p.quantity < 10

        ORDER BY p.quantity ASC

    """)

    rows = cursor.fetchall()

    conn.close()

    return [dict(row) for row in rows]




# User Profiles
def get_admin_profile(admin_id):

    connection = sqlite3.connect(DATABASE)
    connection.row_factory = sqlite3.Row

    cursor = connection.cursor()

    cursor.execute("""
        SELECT
            id,
            name,
            phone,
            password,
            created_at,
            updated_at
        FROM admins
        WHERE id = ?
    """, (admin_id,))

    admin = cursor.fetchone()

    connection.close()

    if admin is None:
        return None

    return dict(admin)


#update profile
def update_admin_profile(admin_id, name, phone, password):

    connection = sqlite3.connect(DATABASE)

    cursor = connection.cursor()

    cursor.execute("""
        UPDATE admins
        SET
            name = ?,
            phone = ?,
            password = ?,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = ?
    """,
    (
        name,
        phone,
        password,
        admin_id
    ))

    connection.commit()

    success = cursor.rowcount > 0

    connection.close()

    return success
