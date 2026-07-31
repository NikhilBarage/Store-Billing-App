import sqlite3


DATABASE = "store.db"


def get_connection():

    conn = sqlite3.connect(DATABASE)
    conn.row_factory = sqlite3.Row

    return conn



# Get all products

def get_all_products():

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""
        SELECT

            p.id,
            p.product_name,
            c.category_name,
            p.price,
            p.quantity

        FROM products p

        INNER JOIN categories c

        ON p.category_id = c.id

        ORDER BY p.id DESC

    """)


    rows = cursor.fetchall()

    conn.close()


    return [dict(row) for row in rows]





# Dropdown products

def get_product_dropdown():

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""
        SELECT

            id,
            product_name

        FROM products

        ORDER BY product_name

    """)


    rows = cursor.fetchall()

    conn.close()


    return [dict(row) for row in rows]






# Get single product

def get_product(product_id):

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""
        SELECT *

        FROM products

        WHERE id = ?

    """,(product_id,))


    row = cursor.fetchone()

    conn.close()


    return dict(row)






# Update product

def update_product(product_id, price, quantity):

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""

        UPDATE products

        SET

        price = ?,

        quantity = ?

        WHERE id = ?

    """,
    (
        price,
        quantity,
        product_id
    ))


    conn.commit()

    conn.close()



    return True






# Delete product

def delete_product(product_id):

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""

        DELETE FROM products

        WHERE id = ?

    """,(product_id,))


    conn.commit()

    conn.close()


    return True


# Get Categories

def get_categories():

    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("""
        SELECT
            id,
            category_name
        FROM categories
        ORDER BY category_name
    """)

    rows = cursor.fetchall()

    conn.close()

    return [dict(row) for row in rows]




# Add New Product

def add_product(category_id, product_name, price, quantity):

    conn = get_connection()
    cursor = conn.cursor()


    cursor.execute("""
        INSERT INTO products
        (
            category_id,
            product_name,
            price,
            quantity
        )

        VALUES
        (?,?,?,?)

    """,
    (
        category_id,
        product_name,
        price,
        quantity
    ))


    conn.commit()

    conn.close()


    return True