document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (productCode) => ({
        productCode: productCode,
        product: null,
        loading: true,
        showPreview: false,
        addedToCart: false,

        init() {
            updateCartItemCount();
            this.getProductDetails(this.productCode);
        },

        getProductDetails(code) {
            this.loading = true;
            $.getJSON("/api/products/" + code, (data) => {
                this.product = data;
                this.loading = false;
            }).fail((err) => {
                console.error("Failed to fetch product details", err);
                this.loading = false;
            });
        },

        addToCart() {
            addProductToCart(this.product);
            this.addedToCart = true;
            setTimeout(() => { this.addedToCart = false; }, 2000);
        }
    }))
});
