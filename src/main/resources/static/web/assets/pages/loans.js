const { createApp } = Vue

createApp({
    data() {
        return {
            client: undefined,
            account: {},
        }
    },
    created() {

        this.loadData()
    },
    methods: {
        loadData() {
            console.log("LoadData");
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts.toSorted((a, b) => (a.creationDate) - (b.creationDate))
                    console.log(this.client)
                })
                .catch(error => {
                    console.log(error)
                })
        },
        printAccount() {
            console.log(this.account)
        },
        payLoan(loan) {
            console.log(loan)
            Swal.fire({
                title: "Confirm",
                text: "You will pay for the following:",
                icon: "warning",
                showCancelButton: true,
                html: `
                <div class="text-center text-white">
                    <strong>${loan.amount/loan.payments}</strong>
                    <br>
                    <p>will be deducted from</p>
                    <strong>${this.account.number}</strong> to pay for <br>
                    <strong>Loan:</strong> ${loan.name}<br>
                    <strong>Debt:</strong> ${loan.amount}<br>
                    <strong>Payments remaining:</strong> ${loan.payments}<br>
                `,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Confirm",
                cancelButtonText: "cancel"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch("/api/clients/current/accounts/payLoan?id=" + loan.id + "&accountNumber=" + this.account.number)
                        .then(response => {
                            console.log(response)
                            this.loadData()
                            Swal.fire({
                                title: "Paid!",
                                text: "Your loan has been paid",
                                html: `
                                <div class="text-center text-white">
                                    <strong>Loan:</strong> ${loan.name}<br>
                                    <strong>Debt:</strong> ${loan.amount}<br>
                                    <strong>Payments remaining:</strong> ${loan.payments}<br>
                                </div>
                                `,
                                icon: "success"
                            })
                        })
                        .catch(error => {
                            Swal.fire({
                                title: 'Error!',
                                text: error.request.responseText,
                                icon: 'error',
                                confirmButtonText: 'Go back'
                            })
                            console.log(error)
                        })
                }
                else if (result.dismiss === Swal.DismissReason.cancel) {

                    Swal.fire({
                        title: "Canceled",
                        text: "You cancelled your payment",
                        icon: "info"
                    })


                }
            });

        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")