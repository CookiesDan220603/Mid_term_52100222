$(document).ready(function () {
    const cartDetail = $(".cartItems");

    const USER_NAME = localStorage.getItem("username");

    if (!USER_NAME) {
        window.location.href = "/login";
        return;
    }

    $.ajax({
        url: `/cart/get_cart_by_user?id=${USER_NAME}`,
        type: "GET",
        contentType: 'application/json',
        timeout: 10000,
        success: function (data) {
            data.products.forEach(product => {
                cartDetail.append(`
                    <div class="product col-md-4 col-12">
                        <div class="product-details">
                            <div class="product-title">${product.name}</div>
                            <p class="product-description">${product.description}</p>
                        </div>
                        <div class="product-price">${product.price}</div>
                        <div class="product-quantity">
                            <input type="number" value="${product.quantity}" min="1">
                        </div>
                        <div class="product-removal"  data-username="${USER_NAME}">
                            <button class="remove-product" data-id="${product.id}">
                                Remove
                            </button>
                        </div>
                        <div class="product-line-price"><i><strong>SubTotal : </strong></i>${product.price * product.quantity}</div>
                    </div>
                `)
            })
            removeItem();
        },
        error: function (error) {
            console.log(error);
        }
    })

    // remove item
    function removeItem() {
        $(".remove-product").on("click", function (e) {
            e.preventDefault();
            const id = $(this).data("id");
            $.ajax({
                url: `/cart/delete/${id}?username=${USER_NAME}`,
                type: "DELETE",
                async: true,
                contentType: "application/json",
                success: function () {
                    console.log("success");
                    alert("Delete success");
                    window.location.href = "/cart";
                },
                error: function (e) {
                    console.log(e)
                    console.log("error");
                }
            })
        })
    }

})