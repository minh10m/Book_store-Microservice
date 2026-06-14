document.addEventListener('alpine:init', () => {
    Alpine.data('initAdminProductsData', (pageNo) => ({
        pageNo: parseInt(pageNo) || 1,
        products: { data: [], totalElements: 0, totalPages: 0, isFirst: true, isLast: true },
        loading: false,
        isEditing: false,
        isSaving: false,
        currentProduct: {},
        async init() {
            await this.loadProducts(this.pageNo);
        },

        async loadProducts(page) {
            this.loading = true;
            try {
                const response = await fetch(`/admin/api/products?page=${page}`);
                if (!response.ok) throw new Error('Network response was not ok');
                this.products = await response.json();
                this.pageNo = page;
                const newUrl = new URL(window.location);
                newUrl.searchParams.set('page', page);
                window.history.pushState({}, '', newUrl);
            } catch (error) {
                console.error('Error fetching products:', error);
            } finally {
                this.loading = false;
            }
        },


        async deleteProduct(code) {
            if (!confirm('Bạn có chắc chắn muốn xóa sách này?')) return;
            
            try {
                const response = await fetch(`/admin/api/products/${code}`, { method: 'DELETE' });
                if (!response.ok) throw new Error('Delete failed');
                
                await this.loadProducts(this.pageNo);
            } catch (error) {
                console.error('Error deleting product:', error);
                alert('Có lỗi xảy ra khi xóa sản phẩm!');
            }
        }
    }));
});
