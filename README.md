# Inventory-Management-System
Overview

Project description:
A Desktop application that helps small businesses track product stock levels, manage suppliers, and monitor orders. 

Features Overview:

A product database
Real-Time stock monitoring
Alerting system
Role-based access
Order history tracking
Features Specifics:

User Login & Role Management
Admin: Can add/remove users, manage inventory, generate reports, and place orders
Staff: Can view and update stock

Product Management
Add, edit, delete products
Product fields: name SKU, department, quantity, price, supplier, expiry
Filter/sort by department, name, stock level
Stock Alerts
Admins are alerted when stock falls below threshold
Staff can check if product is in stock
Staff (Optionally Admin as well) are alerted when unsold product has expired
Supplier Management
Admins can add/edit/delete suppliers
Admins can Link suppliers with their products
Supplier information stored (Email, Phone, Address)
Order Logs/ Stock Movement
Record when stock is added/removed
Timestamp, user, product, quantity changed
Reversible actions (For errors)
Reports and Summaries
Automatically generates report for given time
Export inventory to CSV or PDF
Summary dashboard that tracks number of product, low-stock items, and recent updates

DataBase:
The database will be handled using SQLite and will store the systems Users, Products, Suppliers, and Stock Logs. 

The Tables should follow these prototypes:

users: (id, username, password_hash, role)
products: (id, name, sku, quantity, category, supplier_id, price, expiry_date)
suppliers: (id, name, contact_email, phone, address)
stock_logs: (id, product_id, user_id, change, timestamp, reason)

Tech Stack: 
UI: Swing
Backend: Java 23
DB: SQLite
Libraries:
Apache PDFBox or iText for PDF exports
ControlsFX or JFoenix for better UI elements
BCrypt for password hashing

UI Concept Design:

Login Screen 
Consists of two text entries for input of the users userName and password and a login button 
to login using entered credentials. The password entry should replace all characters with * to
hide the password the user is entering.

Given a correct combination of userName and password the user will be prompted with the applications main menu.

Main Menu
All primary functions of the application should be accessible from this menu, but accessible functions should 
depend on the users permissions which are based on their role. The following features should be available to their 
respective roles.

Admin
Searching for specific products based on department, name, stock level
Add, edit, delete products
Add new Suppliers
Link suppliers with their products


Staff
Searching for specific products based on department, name, stock level
View if specific products are in stock
Add, delete products

