//     
// Load Profile
//     

async function loadProfile() {

    try {

        const response = await fetch("/dashboard/profile/data");

        if (response.status === 401) {

            window.location.href = "/admin/login";
            return;

        }

        const admin = await response.json();

        document.getElementById("adminId").value = admin.id;
        document.getElementById("name").value = admin.name;
        document.getElementById("phone").value = admin.phone;
        document.getElementById("password").value = admin.password;

        document.getElementById("createdAt").value = admin.created_at;

        document.getElementById("updatedAt").value =
            admin.updated_at ? admin.updated_at : admin.created_at;

        document.getElementById("profileTitle").innerText = admin.name;

    }

    catch (error) {

        console.error(error);

        alert("Unable to load profile.");

    }

}



//     
// Update Profile
//     

document.getElementById("updateProfile").addEventListener("click", async function () {

    const name = document.getElementById("name").value.trim();

    const phone = document.getElementById("phone").value.trim();

    const password = document.getElementById("password").value.trim();


    if (name === "") {

        alert("Enter Name");

        return;

    }

    if (phone === "") {

        alert("Enter Phone Number");

        return;

    }

    if (password === "") {

        alert("Enter Password");

        return;

    }


    try {

        const response = await fetch("/dashboard/profile/update", {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify({

                name: name,

                phone: phone,

                password: password

            })

        });


        const result = await response.json();

        if (result.success) {

            alert(result.message);

            if (result.logout) {

                window.location.href = "/login";

            }

        }
        else {

            alert(result.message);

        }

    }

    catch (error) {

        console.error(error);

        alert("Something went wrong.");

    }

});



//     
// Show / Hide Password
//     

document.getElementById("togglePassword").addEventListener("click", function () {

    const password = document.getElementById("password");

    const icon = this.querySelector("i");


    if (password.type === "password") {

        password.type = "text";

        icon.classList.remove("bi-eye");

        icon.classList.add("bi-eye-slash");

    }

    else {

        password.type = "password";

        icon.classList.remove("bi-eye-slash");

        icon.classList.add("bi-eye");

    }

});



//     
// Load on Page Start
//     

window.onload = function () {

    loadProfile();

};