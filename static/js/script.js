console.log("Script Added...")

// show available price & stock of selected product
document.getElementById("product")
.addEventListener("change", function(){

    let productId = this.value;


    if(productId === ""){
        document.getElementById("price").value = "";
        document.getElementById("stock").value = "";
        return;
    }


    fetch(`/get-product/${productId}`)

    .then(response => response.json())

    .then(data => {

        document.getElementById("price").innerText =
            "Price : " + data.price;

        document.getElementById("stock").innerText =
            "Available Stock : " + data.stock;

    });

});



// add products to bill
let billItems = [];
let grandTotal = 0;

document.getElementById("addProductBtn").addEventListener("click", function () {

    const productId = document.getElementById("product").value;
    const quantity = document.getElementById("quantity").value;

    if (productId === "" || quantity === "") {
        alert("Select product and enter quantity.");
        return;
    }

    fetch("/add-product", {

        method: "POST",

        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },

        body:
            "product_id=" + encodeURIComponent(productId) +
            "&quantity=" + encodeURIComponent(quantity)

    })

    .then(response => response.json())

    .then(data => {

        if (!data.success) {
            alert(data.message);
            return;
        }

        billItems.push({
            product_id: data.product_id,
            product_name: data.product_name,
            quantity: data.quantity,
            price: data.price,
            total: data.total
        });

        const table = document.getElementById("billTable");

        table.innerHTML += `
            <tr>
                <td>${data.product_name}</td>
                <td>${data.quantity}</td>
                <td>${data.price}</td>
                <td>${data.total}</td>
            </tr>
        `;

        // Update Grand Total
        grandTotal += data.total;

        document.getElementById("grandTotal").innerText =
            "Grand Total : Rs. " + grandTotal;

        // Clear previous input
        document.getElementById("product").selectedIndex = 0;
        document.getElementById("quantity").value = "";
        document.getElementById("price").innerText = ""
        document.getElementById("stock").innerText = ""

        // Focus back to product dropdown
        document.getElementById("product").focus();

    });

});


//create final bill
document.getElementById("createBillBtn")
.addEventListener("click", function () {


    if (billItems.length === 0) {

        alert("No products in bill.");
        return;

    }


    fetch("/create-bill", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({
            items: billItems,
            grandTotal: grandTotal
        })

    })


    .then(response => response.blob())


    .then(blob => {


        let pdfURL = window.URL.createObjectURL(blob);


        let a = document.createElement("a");

        a.href = pdfURL;

        a.download = "Bill.pdf";

        a.click();


        // Clear bill after successful creation

        billItems = [];

        grandTotal = 0;


        document.getElementById("billTable").innerHTML = "";

        document.getElementById("grandTotal").innerText =
            "Grand Total : Rs. 0";


    });
});

