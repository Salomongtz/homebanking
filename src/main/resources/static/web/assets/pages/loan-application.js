const { createApp } = Vue

createApp({
    data() {
        return {
            transferType: 'ownAccount',
            destinationAccountNumber: '',
            amount: 0,
            description: '',
            accounts: [],
            originAccount: '',
            originAccountBalance: 0,
            loans: [],
            loanType: '',
            payments: 1,
            interest: 1.2
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
                }).catch((error) => {
                    console.log(error)
                })
            axios.get("/api/loans")
                .then((response) => {
                    this.loans = response.data
                    console.log(this.loans);
                })
        },
        createLoan() {
            console.log(this.loanType)
            Swal.fire({
                title: "Please confirm the following data is correct: ",
                html: `
                <div class="text-center text-white">
                  <strong>Loan Type:</strong> ${this.loanType.name}<br>
                  <strong>Interest:</strong> ${Math.round(this.loanType.interestRate % 1 * 100)}%<br>
                  <strong>Destination account:</strong> ${this.destinationAccountNumber}<br>
                  <strong>Amount:</strong> ${this.amount.toLocaleString('en-US', {
                    style: 'currency', currency:
                        'USD'
                })}<br>
                  <strong>Payments:</strong> ${this.payments}<br>
                  <strong>Monthly payment:</strong> ${((this.amount * this.loanType.interestRate) / this.payments).toLocaleString('en-US', {
                    style: 'currency', currency:
                        'USD'
                })}
                </div>
              `,
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Confirm transaction"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans', {
                        id: this.loanType.id,
                        amount: this.amount,
                        payments: this.payments,
                        accountNumber: this.destinationAccountNumber,
                    })
                        .then((response) => {
                            console.log(response)
                            Swal.fire({
                                title: 'Success!',
                                text: 'Transaction completed!',
                                icon: 'success',
                                confirmButtonText: 'Cool'
                            })
                            this.loadData()
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
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        },
        maxAmount() {
            if (this.value.length > this.loanType.maxAmount) {
                this.value = this.value.slice(0, this.loanType.maxAmount);
            }
        }
    }
}
).mount("#app")