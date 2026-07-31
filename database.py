import sqlite3

connection = sqlite3.connect("store.db")
cursor = connection.cursor()

cursor.execute("PRAGMA foreign_keys = ON")


# Categories

cursor.execute("""
CREATE TABLE IF NOT EXISTS categories(

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    category_name TEXT NOT NULL UNIQUE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

)
""")


# Products

cursor.execute("""
CREATE TABLE IF NOT EXISTS products(

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    product_name TEXT NOT NULL,

    category_id INTEGER NOT NULL,

    price REAL NOT NULL,

    quantity INTEGER NOT NULL DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(category_id)
        REFERENCES categories(id)

)
""")


# Admin

cursor.execute("""
CREATE TABLE IF NOT EXISTS admins(

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    name TEXT NOT NULL,

    phone TEXT UNIQUE NOT NULL,

    password TEXT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

)
""")


# Bills

cursor.execute("""
CREATE TABLE IF NOT EXISTS bills(

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    bill_number TEXT UNIQUE NOT NULL,

    total_amount REAL NOT NULL,

    payment_mode TEXT DEFAULT 'Cash',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

)
""")


# Bill Items

cursor.execute("""
CREATE TABLE IF NOT EXISTS bill_items(

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    bill_id INTEGER NOT NULL,

    product_id INTEGER NOT NULL,

    quantity INTEGER NOT NULL,

    price REAL NOT NULL,

    total REAL NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY(bill_id)
        REFERENCES bills(id)
        ON DELETE CASCADE,

    FOREIGN KEY(product_id)
        REFERENCES products(id)

)
""")


connection.commit()
connection.close()

print("Database Created Successfully.")