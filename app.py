from io import BytesIO

from flask import Flask, render_template, jsonify, request, send_file, redirect, session
import sqlite3

from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer
from reportlab.lib.styles import getSampleStyleSheet
from io import BytesIO

import dashboard_services
import product_services


app = Flask(__name__)
app.secret_key = "store_billing_secrete_key"

DATABASE = "store.db"


# Home Page - Load Products
@app.route("/")
def home():

    with sqlite3.connect(DATABASE) as connection:

        cursor = connection.cursor()

        cursor.execute("""
            SELECT id, product_name
            FROM products
            ORDER BY product_name
        """)

        products = cursor.fetchall()


    return render_template(
        "FirstBillPage.html",
        products=products
    )



# Get Product Details when selecting product
@app.route("/get-product/<int:id>")
def get_product(id):

    with sqlite3.connect(DATABASE) as connection:

        cursor = connection.cursor()

        cursor.execute("""
            SELECT price, quantity
            FROM products
            WHERE id = ?
        """, (id,))

        product = cursor.fetchone()


    if product:

        return jsonify({
            "success": True,
            "price": product[0],
            "stock": product[1]
        })


    return jsonify({
        "success": False,
        "message": "Product not found."
    })



# Add Product to Bill
@app.route("/add-product", methods=["POST"])
def add_product():

    product_id = request.form.get("product_id")
    quantity = request.form.get("quantity")


    # Quantity validation
    if not quantity:

        return jsonify({
            "success": False,
            "message": "Enter quantity."
        })


    purchase_quantity = int(quantity)
    with sqlite3.connect(DATABASE) as connection:

        cursor = connection.cursor()


        cursor.execute("""
            SELECT
                id,
                product_name,
                price,
                quantity
            FROM products
            WHERE id = ?
        """, (product_id,))


        product = cursor.fetchone()



    if product is None:

        return jsonify({
            "success": False,
            "message": "Product not found."
        })



    product_id = product[0]
    product_name = product[1]
    price = product[2]
    available_stock = product[3]



    # Stock validation
    if available_stock <= 0:

        return jsonify({
            "success": False,
            "message": "Out of Stock."
        })



    if purchase_quantity > available_stock:

        return jsonify({
            "success": False,
            "message": f"Only {available_stock} items available."
        })



    total = purchase_quantity * price



    return jsonify({

        "success": True,

        "product_id": product_id,

        "product_name": product_name,

        "price": price,

        "quantity": purchase_quantity,

        "total": total

    })



#Generate the Final Bill with PDF & minus stocks also
@app.route("/create-bill", methods=["POST"])
def create_bill():

        data = request.json

        items = data["items"]
        grand_total = data["grandTotal"]

        payment_mode = data.get("paymentMode", "Cash")

        connection = sqlite3.connect("store.db")
        cursor = connection.cursor()

        try:

            # Generate Bill Number

            import datetime

            bill_number = "BILL" + datetime.datetime.now().strftime("%Y%m%d%H%M%S")

            # Save Bill

            cursor.execute("""

                INSERT INTO bills(

                    bill_number,

                    total_amount,

                    payment_mode

                )

                VALUES (?, ?, ?)

            """, (

                bill_number,

                grand_total,

                payment_mode

            ))

            bill_id = cursor.lastrowid

            # Save Bill Items

            for item in items:
                cursor.execute("""

                    INSERT INTO bill_items(

                        bill_id,

                        product_id,

                        quantity,

                        price,

                        total

                    )

                    VALUES (?, ?, ?, ?, ?)

                """, (

                    bill_id,

                    item["product_id"],

                    item["quantity"],

                    item["price"],

                    item["total"]

                ))

                # Reduce Product Stock

                cursor.execute("""

                    UPDATE products

                    SET quantity = quantity - ?

                    WHERE id = ?

                """, (

                    item["quantity"],

                    item["product_id"]

                ))

            connection.commit()

        except Exception as e:

            connection.rollback()

            return jsonify({

                "success": False,

                "message": str(e)

            })

        finally:

            connection.close()

        # Generate PDF
        pdf = BytesIO()

        doc = SimpleDocTemplate(pdf)

        elements = []

        styles = getSampleStyleSheet()

        elements.append(
            Paragraph("STORE BILL", styles["Title"])
        )

        elements.append(Spacer(1, 20))

        # Table Header
        table_data = [

            [
                "Product Name",
                "Quantity",
                "Price",
                "Total"
            ]

        ]

        # Add Products
        for item in items:
            table_data.append([

                item["product_name"],

                item["quantity"],

                item["price"],

                item["total"]

            ])

        # Create Table
        table = Table(table_data)

        # Table Border
        table.setStyle(

            TableStyle([

                ('GRID', (0, 0), (-1, -1), 1, None),

                ('ALIGN', (1, 1), (-1, -1), 'CENTER'),

            ])

        )

        elements.append(table)

        elements.append(Spacer(1, 20))

        elements.append(

            Paragraph(
                f"Grand Total : Rs. {grand_total}",
                styles["Heading2"]
            )

        )

        doc.build(elements)

        pdf.seek(0)

        return send_file(

            pdf,

            as_attachment=True,

            download_name="Bill.pdf",

            mimetype="application/pdf"

        )


@app.before_request
def protect_dashboard():

    if request.path.startswith("/dashboard"):

        if not session.get("logged_in"):

            return redirect("/login")

#Admin Login page
@app.route("/login", methods=["GET", "POST"])
def loginAdmin():
    if request.method == "POST":

        phone = request.form.get("phone")

        password = request.form.get("password")

        connection = sqlite3.connect("store.db")

        cursor = connection.cursor()

        cursor.execute("""
                SELECT id,name
                FROM admins
                WHERE phone=? AND password=?
            """,
                       (
                           phone,
                           password
                       ))

        admin = cursor.fetchone()

        connection.close()

        if admin:
            session["logged_in"] = True
            session["admin_id"] = admin[0]

            session["admin_name"] = admin[1]

            return redirect("/dashboard")

        return "Invalid phone or password"

    return render_template("AdminLogin.html")


@app.route("/logout")
def logout():

    session.clear()

    return redirect("/login")


#Admin register page
@app.route("/reg", methods=["GET", "POST"])
def registerAdmin():
    if request.method == "POST":

        name = request.form.get("name")
        phone = request.form.get("phone")
        password = request.form.get("password")

        connection = sqlite3.connect("store.db")
        cursor = connection.cursor()

        # Check existing admin
        cursor.execute("""
                SELECT COUNT(*) FROM admins
            """)

        admin_count = cursor.fetchone()[0]

        if admin_count >= 1:
            connection.close()

            return """
                <h3>Admin already registered.</h3>
                <a href="/login">Go to Login</a>
                """

        # Name validation
        if not name.replace(" ", "").isalpha():
            connection.close()

            return "Name should contain only alphabets"
        # Phone validation
        if not phone.isdigit() or len(phone) != 10:
            connection.close()

            return "Enter valid 10 digit phone number"

        # Insert admin

        cursor.execute("""
                    INSERT INTO admins(name, phone, password)
                    VALUES(?,?,?)
                """,
                (
                    name,
                    phone,
                    password
                ))

        connection.commit()

        connection.close()

        return redirect("/login")

    return render_template("AdminRegister.html")


#Profile
@app.route("/dashboard/profile")
def profile():

    if not session.get("logged_in"):
        return redirect("/admin/login")

    return render_template("Profile.html")


#get profile data
@app.route("/dashboard/profile/data")
def profile_data():

    if not session.get("logged_in"):
        return jsonify({
            "success": False,
            "message": "Unauthorized"
        }), 401

    admin = dashboard_services.get_admin_profile(session["admin_id"])

    return jsonify(admin)



# Update Profile
@app.route("/dashboard/profile/update", methods=["POST"])
def profile_update():

    if not session.get("logged_in"):
        return jsonify({
            "success": False,
            "message": "Unauthorized"
        }), 401

    data = request.get_json()

    success = dashboard_services.update_admin_profile(

        session["admin_id"],

        data["name"],

        data["phone"],

        data["password"]

    )

    if success:

        # Logout the current session
        session.clear()

        return jsonify({

            "success": True,

            "logout": True,

            "message": "Profile updated successfully. Please login again."

        })

    return jsonify({

        "success": False,

        "message": "Unable to update profile."

    }), 400



#Dashboard
@app.route("/dashboard")
def dashboard():
    return render_template("dashboard.html")


@app.route("/dashboard/cards")
def cards():
    return jsonify(dashboard_services.get_cards())


@app.route("/dashboard/sales-chart")
def sales_chart():

    from_date = request.args.get("from")
    to_date = request.args.get("to")

    print("FROM:", from_date)
    print("TO:", to_date)

    data = dashboard_services.get_sales_chart(
        from_date,
        to_date
    )

    print("SALES DATA:", data)

    return jsonify(data)


@app.route("/dashboard/stock-chart")
def stock_chart():

    return jsonify(
        dashboard_services.get_stock_chart()
    )


@app.route("/dashboard/low-stock")
def low_stock():

    return jsonify(
        dashboard_services.get_low_stock()
    )



#Products
@app.route("/dashboard/products")
def products():

    return render_template("products.html")




@app.route("/products/list")
def product_list():

    return jsonify(
        product_services.get_all_products()
    )




@app.route("/products/dropdown")
def product_dropdown():

    return jsonify(
        product_services.get_product_dropdown()
    )





@app.route("/products/<int:id>")
def single_product(id):

    return jsonify(
        product_services.get_product(id)
    )





@app.route("/products/update", methods=["POST"])
def update_product():

    data = request.json


    product_services.update_product(

        data["id"],

        data["price"],

        data["quantity"]

    )


    return jsonify({
        "message":"Product Updated"
    })





@app.route("/products/delete/<int:id>", methods=["DELETE"])
def delete_product(id):

    product_services.delete_product(id)


    return jsonify({
        "message":"Product Deleted"
    })


@app.route("/categories/list")
def category_list():

    return jsonify(
        product_services.get_categories()
    )





@app.route("/products/add", methods=["POST"])
def add_new_product():


    data = request.json


    product_services.add_product(

        data["category_id"],

        data["product_name"],

        data["price"],

        data["quantity"]

    )


    return jsonify({

        "message":"Product Added Successfully"

    })



if __name__ == "__main__":
    app.run(debug=True)