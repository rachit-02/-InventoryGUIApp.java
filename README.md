# 📦 Inventory Management System (Java Swing)

A desktop-based **Inventory Management System** built using **Core Java** and **Java Swing**. The project demonstrates key object-oriented programming concepts, GUI development, multithreading, generics, and file handling.

The application enables users to manage electronic products through an interactive graphical interface by adding, removing, searching, and displaying inventory items.

---

## 🚀 Features

* ➕ Add new electronic products to inventory
* ❌ Remove products using Product ID
* 🔍 Search products by product name
* 📋 Display all products in a table format
* 💾 Save inventory data using object serialization
* 📂 Load inventory data from a file
* ⚡ Multithreaded product search using `ExecutorService`
* 🧠 Generic inventory management system
* 🎨 User-friendly Java Swing interface
* 📊 Calculate average product price using a static nested class

---

## 🛠️ Tech Stack

* **Java (Core Java)**
* **Java Swing** for GUI development
* **Collections Framework**
* **Object Serialization**
* **Multithreading (`ExecutorService`)**
* **Java Generics**

---

## 📚 OOP Concepts Implemented

### 🔹 Abstraction

The abstract `Product` class provides a common blueprint for all products.

```java
abstract class Product implements Serializable
```

### 🔹 Inheritance

`ElectronicProduct` extends the `Product` class.

```java
class ElectronicProduct extends Product
```

### 🔹 Interfaces

The `InventoryOperations` interface defines inventory operations.

```java
interface InventoryOperations<T extends Product>
```

### 🔹 Generics

The inventory system supports generic product types.

```java
class InventoryManagementSystem<T extends Product>
```

### 🔹 Static Nested Class

The `Stats` class calculates statistics.

```java
static class Stats
```

---

## ⚙️ Functionalities

### Add Product

Users can add a new product by entering:

* Product ID
* Product Name
* Category
* Quantity
* Price

---

### Remove Product

Users can remove a product using its unique Product ID.

---

### Search Product

Products can be searched by name. The search operation runs in a separate thread using:

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
```

---

### Display Inventory

All products are displayed in a JTable with the following columns:

* ID
* Name
* Category
* Quantity
* Price

---

### Save Inventory

Inventory data is saved using Java Object Serialization.

```java
ObjectOutputStream
```

---

### Load Inventory

Previously saved inventory can be restored using:

```java
ObjectInputStream
```

---

## 📂 Project Structure

```text
inventoryapp/
│
├── InventoryOperations.java
├── Product.java
├── ElectronicProduct.java
├── InventoryManagementSystem.java
└── InventoryGUIApp.java
```

---

## 🖥️ GUI Components

* `JFrame`
* `JTable`
* `JButton`
* `JTextField`
* `JTextArea`
* `JPanel`
* `JOptionPane`
* `JScrollPane`

---

## 📊 Sample Products Included

The application starts with 20 preloaded electronic products, including:

* Smartphone
* Laptop
* Headphones
* Monitor
* Keyboard
* Mouse
* Smartwatch
* Tablet
* Webcam
* Printer
* Router
* SSD
* Camera
* TV
* VR Headset

---

## ▶️ How to Run

### Clone the repository

```bash
git clone https://github.com/your-username/inventory-management-system.git
```

### Open the project

Open the project in:

* IntelliJ IDEA
* Eclipse
* NetBeans

### Compile and run

```bash
javac InventoryGUIApp.java
java InventoryGUIApp
```

---

## 🎯 Learning Outcomes

This project demonstrates:

* Object-Oriented Programming
* GUI development with Java Swing
* Generic programming
* Exception handling
* Java Collections
* File handling and serialization
* Multithreading
* Event-driven programming

---

## 📜 License

This project is intended for educational and learning purposes.
