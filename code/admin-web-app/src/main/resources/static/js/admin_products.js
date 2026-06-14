document.addEventListener('alpine:init', () => {
    Alpine.data('initAdminProductsData', (pageNo) => ({
        pageNo: parseInt(pageNo) || 1,
        products: { data: [], totalElements: 0, totalPages: 0, isFirst: true, isLast: true },
        loading: false,
        isEditing: false,
        isSaving: false,
        currentProduct: {},
        modalInstance: null,

        async init() {
            this.modalInstance = new bootstrap.Modal(this.$refs.productModal);
            await this.loadProducts(this.pageNo);
        },

        async loadProducts(page) {
            this.loading = true;
            try {
                const response = await fetch(`/api/products?page=${page}`);
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

        openCreateModal() {
            this.isEditing = false;
            this.currentProduct = {
                code: '',
                name: '',
                price: null,
                imageUrl: '',
                previewText: '',
                description: ''
            };
            this.modalInstance.show();
        },

        openEditModal(product) {
            this.isEditing = true;
            this.currentProduct = { ...product };
            this.modalInstance.show();
        },

        async saveProduct() {
            this.isSaving = true;
            try {
                const method = this.isEditing ? 'PUT' : 'POST';
                const url = this.isEditing ? `/api/products/${this.currentProduct.code}` : '/api/products';
                
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(this.currentProduct)
                });
                
                if (!response.ok) throw new Error('Save failed');
                
                this.modalInstance.hide();
                await this.loadProducts(this.pageNo);
            } catch (error) {
                console.error('Error saving product:', error);
                alert('Có lỗi xảy ra khi lưu sản phẩm!');
            } finally {
                this.isSaving = false;
            }
        },

        async deleteProduct(code) {
            if (!confirm('Bạn có chắc chắn muốn xóa sách này?')) return;
            
            try {
                const response = await fetch(`/api/products/${code}`, { method: 'DELETE' });
                if (!response.ok) throw new Error('Delete failed');
                
                await this.loadProducts(this.pageNo);
            } catch (error) {
                console.error('Error deleting product:', error);
                alert('Có lỗi xảy ra khi xóa sản phẩm!');
            }
        }
    }));
});
