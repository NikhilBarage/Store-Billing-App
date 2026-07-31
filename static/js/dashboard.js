
// All Functions
async function loadCards() {

    try {

        const response = await fetch("/dashboard/cards");

        const data = await response.json();

        document.getElementById("totalProducts").innerText =
            data.total_products;

        document.getElementById("availableStock").innerText =
            data.available_stock;

        document.getElementById("lowStock").innerText =
            data.low_stock;

    }

    catch (error) {

        console.log(error);

    }

}

// Load sale Chart
let salesChart;

async function loadSalesChart() {

    const from = document.getElementById("fromDate").value;
    const to = document.getElementById("toDate").value;

    try {

        const response = await fetch(
            `/dashboard/sales-chart?from=${from}&to=${to}`
        );

        const data = await response.json();

        console.log("Sales Chart Data:", data);

        const labels = data.map(item => item.product_name);
        const quantity = data.map(item => Number(item.sold_quantity));

        if (salesChart) {
            salesChart.destroy();
        }

        const ctx = document.getElementById("salesChart").getContext("2d");

        salesChart = new Chart(ctx, {

            type: "bar",

            data: {

                labels: labels,

                datasets: [

                    {
                        label: "Products Sold",

                        data: quantity,

                        backgroundColor: "#0d6efd",

                        borderColor: "#0d6efd",

                        borderWidth: 1,

                        borderRadius: 5
                    }

                ]

            },

            options: {

                responsive: true,

                maintainAspectRatio: false,

                scales: {

                    y: {

                        beginAtZero: true,

                        ticks: {

                            precision: 0,

                            stepSize: 1

                        },

                        title: {

                            display: true,

                            text: "Quantity Sold"

                        }

                    },

                    x: {

                        title: {

                            display: true,

                            text: "Products"

                        }

                    }

                },

                plugins: {

                    legend: {

                        display: false

                    },

                    tooltip: {

                        callbacks: {

                            label: function (context) {

                                return " Sold : " + context.raw;

                            }

                        }

                    }

                }

            }

        });

    }

    catch (error) {

        console.error("Sales Chart Error:", error);

    }

}

// Stock Chart
let stockChart;

async function loadStockChart() {

    const response =
        await fetch("/dashboard/stock-chart");

    const data = await response.json();

    const labels =
        data.map(item => item.product_name);

    const quantity =
        data.map(item => item.quantity);

    if (stockChart) {

        stockChart.destroy();

    }

    stockChart = new Chart(

        document.getElementById("stockChart"),

        {

            type: "doughnut",

            data: {

                labels,

                datasets: [

                    {

                        data: quantity

                    }

                ]

            },

            options: {

                responsive: true

            }

        }

    );

}



async function loadLowStockTable() {

    const response =
        await fetch("/dashboard/low-stock");

    const data = await response.json();

    const tbody =
        document.getElementById("lowStockTable");

    tbody.innerHTML = "";

    if (data.length === 0) {

        tbody.innerHTML = `

        <tr>

            <td colspan="3" class="text-center">

                No Low Stock Products

            </td>

        </tr>

        `;

        return;

    }

    data.forEach(product => {

        tbody.innerHTML += `

        <tr>

            <td>${product.product_name}</td>

            <td>${product.category_name}</td>

            <td>${product.quantity}</td>

        </tr>

        `;

    });

}




document.addEventListener("DOMContentLoaded", () => {

    setDefaultDates();

    loadDashboard();

    document
        .getElementById("generateReport")
        .addEventListener("click", loadDashboard);

});


// Default Today's Date

function setDefaultDates() {

    const today = new Date().toISOString().split("T")[0];

    document.getElementById("fromDate").value = today;

    document.getElementById("toDate").value = today;

}


// Load Everything

function loadDashboard() {

    loadCards();

    loadSalesChart();

    loadStockChart();

    loadLowStockTable();

}



async function loadProfile() {

    const response = await fetch("/dashboard/profile");

    const admin = await response.json();

    document.getElementById("adminId").value = admin.id;

    document.getElementById("adminName").value = admin.name;

    document.getElementById("adminPhone").value = admin.phone;

    document.getElementById("createdAt").value = admin.created_at;

    document.getElementById("updatedAt").value = admin.updated_at || admin.created_at;

}

document
.getElementById("profileModal")
.addEventListener("show.bs.modal", loadProfile);


document
.getElementById("updateProfile")
.addEventListener("click", async () => {

    const body = {

        name:
        document.getElementById("adminName").value,

        phone:
        document.getElementById("adminPhone").value,

        password:
        document.getElementById("adminPassword").value

    };

    const response = await fetch(

        "/dashboard/profile/update",

        {

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify(body)

        }

    );

    const result = await response.json();

    alert(result.message);

    loadProfile();

});