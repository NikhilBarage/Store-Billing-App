console.log("Products JS Loaded");


let selectedProductId = null;



// Load Product Dropdown

function loadProductDropdown(){

    fetch("/products/dropdown")

    .then(response => response.json())

    .then(data => {


        let dropdown = document.getElementById("productSelect");


        dropdown.innerHTML = `
            <option value="">
                Choose Product
            </option>
        `;



        data.forEach(product => {


            dropdown.innerHTML += `

                <option value="${product.id}">

                    ${product.product_name}

                </option>

            `;


        });


    });


}





// Product Selected

document.getElementById("productSelect")
.addEventListener("change", function(){


    let id = this.value;


    if(id==""){

        return;

    }



    selectedProductId = id;



    fetch(`/products/${id}`)

    .then(response=>response.json())

    .then(product=>{


        document.getElementById("price").value =
        product.price;



        document.getElementById("quantity").value =
        product.quantity;


    });



});







// Update Product


document.getElementById("updateBtn")
.addEventListener("click",function(){



    let price =
    document.getElementById("price").value;



    let quantity =
    document.getElementById("quantity").value;



    if(selectedProductId == null){

        alert("Select Product First");

        return;

    }




    fetch("/products/update",{


        method:"POST",


        headers:{

            "Content-Type":"application/json"

        },


        body:JSON.stringify({


            id:selectedProductId,

            price:price,

            quantity:quantity


        })


    })

    .then(response=>response.json())


    .then(data=>{


        alert(data.message);


        loadProducts();


    });



});


function loadCategories(){

    fetch("/categories/list")

    .then(response => response.json())

    .then(categories => {


        console.log("Categories:", categories);


        let dropdown =
        document.getElementById("categorySelect");


        dropdown.innerHTML = `
            <option value="">
                Select Category
            </option>
        `;


        categories.forEach(category => {


            dropdown.innerHTML += `

            <option value="${category.id}">
                ${category.category_name}
            </option>

            `;


        });


    })

    .catch(error=>{

        console.log("Category Error:",error);

    });

}


// Load Product Table


function loadProducts(){


    fetch("/products/list")


    .then(response=>response.json())


    .then(products=>{


        let table =
        document.getElementById("productTable");


        table.innerHTML="";



        products.forEach(product=>{


            table.innerHTML += `


            <tr>


                <td>
                    ${product.id}
                </td>


                <td>
                    ${product.product_name}
                </td>



                <td>
                    ${product.category_name}
                </td>



                <td>
                    ₹ ${product.price}
                </td>



                <td>
                    ${product.quantity}
                </td>



                <td>


                    <button 
                    class="btn btn-sm btn-warning"
                    onclick="editProduct(${product.id})">

                    <i class="bi bi-pencil"></i>

                    Edit

                    </button>




                    <button 
                    class="btn btn-sm btn-danger"
                    onclick="deleteProduct(${product.id})">


                    <i class="bi bi-trash"></i>

                    Delete


                    </button>



                </td>



            </tr>


            `;



        });



    });



}









// Edit Button


function editProduct(id){


    document.getElementById("productSelect").value=id;


    selectedProductId=id;



    fetch(`/products/${id}`)


    .then(response=>response.json())


    .then(product=>{


        document.getElementById("price").value =
        product.price;



        document.getElementById("quantity").value =
        product.quantity;


    });



}









// Delete Product


function deleteProduct(id){



    if(!confirm("Delete this product?")){

        return;

    }




    fetch(`/products/delete/${id}`,{


        method:"DELETE"


    })


    .then(response=>response.json())


    .then(data=>{


        alert(data.message);


        loadProducts();


        loadProductDropdown();


    });



}








// Page Load

document.addEventListener("DOMContentLoaded",()=>{

    loadProductDropdown();

    loadProducts();

    loadCategories();

});




document.getElementById("addProductBtn")
.addEventListener("click",()=>{


    let category_id =
    document.getElementById("categorySelect").value;


    let product_name =
    document.getElementById("newProductName").value;


    let price =
    document.getElementById("newProductPrice").value;


    let quantity =
    document.getElementById("newProductQuantity").value;



    if(category_id=="" || product_name=="" || price=="" || quantity==""){

        alert("Fill all fields");

        return;

    }




    fetch("/products/add",{


        method:"POST",


        headers:{

            "Content-Type":"application/json"

        },


        body:JSON.stringify({

            category_id:category_id,

            product_name:product_name,

            price:price,

            quantity:quantity

        })


    })



    .then(response=>response.json())


    .then(data=>{


        alert(data.message);



        document.getElementById("newProductName").value="";

        document.getElementById("newProductPrice").value="";

        document.getElementById("newProductQuantity").value="";



        loadProducts();

        loadProductDropdown();



    });



});



