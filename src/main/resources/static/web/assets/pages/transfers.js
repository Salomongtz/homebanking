const { createApp } = Vue

createApp({
    data() {
        return {
            transferType: 'ownAccount',
            destinationAccountNumber: '',
            originAccountNumber: '',
            amount: 0,
            description: '',
            accounts: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("/api/clients/current/accounts")
                .then((response) => {
                    this.accounts = response.data
                    console.log(this.accounts);
                })
        },
        createTransfer() {
            Swal.fire({
                title: "Please confirm the following data is correct: ",
                html: `
                <div class="text-center text-white">
                  <strong>Origin account:</strong> ${this.originAccountNumber}<br>
                  <strong>Destination account:</strong> ${this.destinationAccountNumber}<br>
                  <strong>Amount:</strong> ${this.amount}<br>
                  <strong>Description:</strong> ${this.description}
                </div>
              `,
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Confirm transaction"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post(`/api/transactions?transactionAmount=${this.amount}&transactionDescription=${this.description}&originAccountNumber=${this.originAccountNumber}&destinationAccountNumber=${this.destinationAccountNumber}`)
                        .then((response) => {
                            console.log(response)
                            Swal.fire({
                                title: 'Success!',
                                text: 'Transaction completed!',
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
            });

        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}
).mount("#app")