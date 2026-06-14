document.addEventListener('alpine:init', () => {
    Alpine.data('initData', (pageNo) => ({
        pageNo: pageNo,
        products: {
            data: []
        },
        loading: false,
        filters: {
            keyword: '',
            category: '',
            minPrice: '',
            maxPrice: ''
        },
        init() {
            this.loadProducts(this.pageNo);
            updateCartItemCount();
        },
        loadProducts(pageNo) {
            this.loading = true;
            $.getJSON("/api/products?page=" + pageNo, (resp) => {
                console.log("Products Resp:", resp)
                this.products = resp;
                this.loading = false;
            }).fail(() => {
                this.loading = false;
            });
        },
        applyFilters() {
            this.loading = true;
            const params = new URLSearchParams();
            if (this.filters.keyword) params.append('keyword', this.filters.keyword);
            if (this.filters.category) params.append('category', this.filters.category);
            if (this.filters.minPrice) params.append('minPrice', this.filters.minPrice);
            if (this.filters.maxPrice) params.append('maxPrice', this.filters.maxPrice);

            // If no filters are applied, just load default products page
            if (params.toString() === '') {
                this.loadProducts(this.pageNo);
                return;
            }

            $.getJSON("/api/products/search?" + params.toString(), (resp) => {
                console.log("Search Resp:", resp)
                this.products = resp;
                this.loading = false;
            }).fail(() => {
                this.products = { data: [] };
                this.loading = false;
            });
        },
        resetFilters() {
            this.filters = {
                keyword: '',
                category: '',
                minPrice: '',
                maxPrice: ''
            };
            this.loadProducts(this.pageNo);
        },
        addToCart(product) {
            addProductToCart(product)
        }
    }))
});