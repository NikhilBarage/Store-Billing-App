# 🧾 Store Billing System App

A smart Android-based solution designed for retail store owners to manage products, generate customer bills, monitor income, and perform inventory operations — all in one offline app.

---

## 🎯 Overview

This Android app simplifies billing and inventory tracking for small to medium-sized retail stores. It features product management, order generation, user authentication, database backup, and analytics tools to streamline daily operations.

---

## ✨ Features

- 🔐 User authentication (Login/Signup)
- ➕ Add & update products
- 🧾 Generate and manage customer bills
- 🛍️ Client order management
- 📊 Track income and orders per client
- 🗂️ Backup and restore database
- 🔍 Search & filter product listings
- 📋 Admin & client dashboard views

---

## 🛠️ Technologies Used

| Category        | Stack                   |
|----------------|--------------------------|
| Language        | Java                     |
| IDE             | Android Studio           |
| UI              | XML                      |
| Database        | SQLite                   |
| Data Backup     | Manual SQLite Export     |

---

🚀 Installation
1. Clone this repository:

    git clone https://github.com/yourusername/store-billing-system

2. Open the project in Android Studio
3. Connect your Android device or emulator
4. Click Run ▶️


⚙️ Configuration
- Ensure you have the correct permissions in AndroidManifest.xml for storage and internet.
- No server is required — data is stored locally using SQLite.
- To enable backup: navigate to the DatabaseBackUp.java module.

📖 Usage
- Login/Register to access the app
- Navigate to:
  🛒 Products → Add/Edit/Delete items
  🧾 Orders → Create customer bills
  📊 Income → View client revenue summary

- Use admin panel for advanced control

🗄️ Database Schema
- Main tables in SQLite:
  Users (id, name, username, password)
  Products (id, name, quantity, price)
  Orders (id, client, items, total, timestamp)
  Clients (id, name, total_income)

- Defined in DBHelper.java

🔒 Security Features
  - Input validation on all forms
  - Local-only access — no external exposure
  - Backup/restore is manually handled to reduce external risk


📁 Project Structure

app/

├── src/

│   └── main/java/com/example/bill/

│       ├── Login.java

│       ├── SignUp.java

│       ├── MainActivity.java

│       ├── addProducts.java

│       ├── updateProducts.java

│       ├── Order.java

│       ├── DBHelper.java

│       └── ...

└── res/

    └── layout/
    
        └── *.xml


👨‍💻 Credits
    - Developed by Nikhil Barage
    - GitHub: @NikhilBarage
    - Contact: nikhilbarage1@gmail.com

