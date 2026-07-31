# 🧾 Store Billing System

A modern **Store Billing & Inventory Management System** built with **Python Flask** and **SQLite**. The application helps retail store owners efficiently manage products, generate bills, monitor inventory, and view sales analytics through an intuitive dashboard.

---

## 📸 Screenshots

- Dashboard
  <img width="1893" height="907" alt="Screenshot 2026-07-31 144745" src="https://github.com/user-attachments/assets/75adf109-44aa-4408-b656-63d13fe63a83" />
  <img width="1896" height="558" alt="Screenshot 2026-07-31 144804" src="https://github.com/user-attachments/assets/af29de85-199d-497d-b3f2-b41c60c8667a" />

- Billing Page
  <img width="1918" height="912" alt="Screenshot 2026-07-31 144613" src="https://github.com/user-attachments/assets/12d06858-9e2f-4e1f-b3d7-caedcbddbc7b" />

- Product Management
  <img width="562" height="557" alt="Screenshot 2026-07-31 144836" src="https://github.com/user-attachments/assets/ba056577-ac6a-49c0-b799-ac69dcb83e4b" />
  <img width="1893" height="893" alt="Screenshot 2026-07-31 144822" src="https://github.com/user-attachments/assets/95a0267b-e620-454a-8397-87ecdc0e2bbc" />

- Admin Profile
  <img width="1918" height="912" alt="Screenshot 2026-07-31 144853" src="https://github.com/user-attachments/assets/e24956cd-82c1-433e-838d-dc81bc200ab8" />

- PDF Bill
  <img width="991" height="575" alt="Screenshot 2026-07-31 144630" src="https://github.com/user-attachments/assets/316be095-f0e4-4a22-973b-b390893993c9" />


---

## ✨ Features

### 📊 Dashboard
- Total Available Products
- Total Available Stock
- Low Stock Products
- Sales Report (Date Range Filter)
- Product Sales Bar Chart
- Inventory Distribution Pie Chart

### 📦 Product Management
- Add Products
- Update Products
- Delete Products
- Stock Management

### 🧾 Billing System
- Generate Customer Bills
- Automatic Stock Deduction
- PDF Bill Generation
- Bill History Storage
- Multiple Products in One Bill

### 👤 Admin Module
- Admin Registration
- Secure Login
- Session Authentication
- Update Profile
- Logout

### 📈 Reports
- Sales Analytics
- Inventory Overview
- Low Stock Alerts

---

## 🛠️ Tech Stack

| Technology   | Description |
|------------  |-------------|
| Python       | Programming Language |
| Flask        | Backend Framework |
| SQLite       | Database |
| HTML5        | Frontend Structure |
| CSS3         | Styling |
| Bootstrap 5  | Responsive UI |
| JavaScript   | Client-side Logic |
| Chart.js     | Dashboard Charts |
| ReportLab    | PDF Bill Generation |

---

## 📁 Project Structure

```
StoreBilling/
│
├── app.py
├── database.py
├── dashboard_services.py
├── product_services.py
├── requirements.txt
├── README.md
│
├── static/
│   ├── css/
│   ├── js/
│   └── uploads/
│
├── templates/
│
└── store.db
```

---

## ⚙️ Installation

### 1. Clone Repository

```bash
git clone https://github.com/NikhilBarage/Store-Billing-App.git

cd Store-Billing-App
```

### 2. Create Virtual Environment

```bash
python -m venv .venv
```

### Windows

```bash
.venv\Scripts\activate
```

### 3. Install Dependencies

```bash
pip install -r requirements.txt
```

### 4. Create Database

```bash
python database.py
```

### 5. Run Application

```bash
python app.py
```

Open your browser:

```
http://127.0.0.1:5000
```

---


## 📊 Dashboard Features

- 📦 Product Count
- 📈 Available Stock
- ⚠️ Low Stock Monitoring
- 📅 Sales Report by Date Range
- 📊 Product Sales Chart
- 🥧 Inventory Distribution Chart

---

## 🔐 Security

- Admin Authentication
- Session-Based Login
- Protected Dashboard Routes
- Input Validation
- SQLite Foreign Key Constraints

---

## 🚀 Future Improvements

- Barcode Scanner
- Customer Management
- Sales Invoice History
- Excel Report Export
- Email Bill
- Dark Mode
- Multi-User Support
- GST Invoice
- Stock Purchase Module
- Monthly Sales Dashboard

---

## 👨‍💻 Author

**Nikhil Barage**

📧 Email: nikhilbarage1@gmail.com

🔗 GitHub: https://github.com/NikhilBarage

---

## 📄 License

This project is licensed under the MIT License.

Feel free to use, modify, and contribute.

---

⭐ If you found this project helpful, consider giving it a **Star** on GitHub.
