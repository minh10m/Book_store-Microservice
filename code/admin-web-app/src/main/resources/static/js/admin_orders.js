document.addEventListener('alpine:init', () => {
    Alpine.data('initAdminOrdersData', () => ({
        orders: [],
        loading: false,

        async init() {
            await this.loadOrders();
        },

        async loadOrders() {
            this.loading = true;
            try {
                const response = await fetch('/admin/api/orders/admin');
                if (!response.ok) throw new Error('Network response was not ok');
                this.orders = await response.json();
            } catch (error) {
                console.error('Error fetching orders:', error);
            } finally {
                this.loading = false;
            }
        },

        async updateStatus(orderNumber, newStatus) {
            if (!newStatus) return;
            try {
                const response = await fetch(`/admin/api/orders/admin/${orderNumber}/status`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'text/plain' },
                    body: newStatus
                });
                
                if (!response.ok) throw new Error('Failed to update status');
                
                await this.loadOrders();
            } catch (error) {
                console.error('Error updating order status:', error);
                alert('Có lỗi xảy ra khi cập nhật trạng thái đơn hàng!');
            }
        }
    }));
});
