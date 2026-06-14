document.addEventListener('alpine:init', () => {
    Alpine.data('initProductFormData', (productCode) => ({
        productCode: productCode,
        isEditing: productCode && productCode.trim() !== '',
        isSaving: false,
        loading: false,
        product: {
            code: '',
            name: '',
            price: null,
            imageUrl: '',
            previewText: '',
            description: ''
        },

        async init() {
            if (this.isEditing) {
                await this.loadProduct();
            }
        },

        async loadProduct() {
            this.loading = true;
            try {
                const response = await fetch(`/admin/api/products/${this.productCode}`);
                if (!response.ok) throw new Error('Fetch failed');
                this.product = await response.json();
            } catch (error) {
                console.error(error);
                alert('Không thể tải thông tin sách!');
            } finally {
                this.loading = false;
            }
        },

        async saveProduct() {
            this.isSaving = true;
            try {
                if (!this.isEditing) {
                    this.product.code = 'P' + Date.now();
                }
                const method = this.isEditing ? 'PUT' : 'POST';
                const url = this.isEditing ? `/admin/api/products/${this.product.code}` : '/admin/api/products';
                
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(this.product)
                });
                
                if (!response.ok) throw new Error('Save failed');
                
                window.location.href = '/admin/products';
            } catch (error) {
                console.error('Error saving product:', error);
                alert('Có lỗi xảy ra khi lưu sản phẩm!');
            } finally {
                this.isSaving = false;
            }
        }
    }));
});
