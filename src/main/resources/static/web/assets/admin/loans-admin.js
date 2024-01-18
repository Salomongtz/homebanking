const { createApp } = Vue

createApp({
    data() {
        return {
            formData: {
                name: '',
                maxAmount: null,
                payments: [],
                interest: null
            }, paymentOptions: [
                { value: 6, label: '6' },
                { value: 12, label: '12' },
                { value: 18, label: '18' },
                { value: 24, label: '24' },
                { value: 36, label: '36' },
                { value: 48, label: '48' },
                { value: 60, label: '60' },
                { value: 72, label: '72' }
            ]
        }
    },
    created() {
        
    },
    methods: {
        
        createLoan() {
            console.log(this.loanType)
            Swal.fire({
                title: "Please confirm the following data is correct: ",
                html: `
                <div class="text-center text-white">
                <strong>Loan Name:</strong> ${this.formData.name}<br>
                <strong>Max Amount:</strong> ${this.formData.maxAmount.toLocaleString('en-US', {
                    style: 'currency', currency:
                        'USD'
                })}<br>
                <strong>Max Payments:</strong> ${this.formData.payments}<br>
                <strong>Interest:</strong> ${Math.round(this.formData.interest % 1 * 100)}%<br>
                </div>
              `,
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Confirm create"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans/new', {
                        name: this.formData.name,
                        maxAmount: this.formData.maxAmount,
                        payments: this.formData.payments,
                        interestRate: this.formData.interest
                    })
                        .then((response) => {
                            console.log(response)
                            Swal.fire({
                                title: 'Success!',
                                text: 'Loan Type Created!',
                                icon: 'success',
                                confirmButtonText: 'Cool'
                            })
                        })
                        .catch((error) => {
                            let msg = "Something happened. Please try again."
                            if (error.response.data != null) {
                                msg = error.response.data
                            }
                            Swal.fire({
                                title: 'Error!',
                                text: msg,
                                icon: 'error',
                                confirmButtonText: 'Go back'
                            })
                            console.log(error)
                        })
                } else {
                    Swal.fire("Transfer cancelled", "", "info");
                }
            })
        },
        logout() {
            axios.post('/api/logout').then(() => window.location.href = '/web/index.html')
        },
    }
}
).mount("#app")