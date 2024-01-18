const { createApp } = Vue

createApp({
    data() {
        return {
            account: {},
            client: {},
            transactions: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            console.log("LoadData");
            const id = new URLSearchParams(window.location.search).get("id")

            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.account = this.client.accounts.find(account => account.id == id)
                    this.transactions = this.account.transactions
                    this.transactions.sort((a, b) => (b.id) - (a.id))
                    console.log(this.client)
                    console.log(this.account)
                    console.log(this.account.transactions);
                })
                .catch(error => {
                    console.log(error)
                })
        }, deleteAccount(id) {
            Swal.fire({
                title: "Are you sure?",
                html: `
                <div class="text-center text-white">
                  <strong>Loan Type:</strong> This account will be deleted<br>
                  <strong>Account Number:</strong> ${this.account.number} <br>
                  <strong>This cannot be undone!</strong>
                </div>
              `,
                icon: "question",
                showCancelButton: true,
                confirmButtonColor: "#3085d6",
                cancelButtonColor: "#d33",
                confirmButtonText: "Yes, delete it!"
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch(`/api/clients/current/accounts/${id}`)
                        .then((response) => {
                            console.log(response)
                            Swal.fire({
                                title: 'Success!',
                                text: 'Account deleted!',
                                icon: 'success',
                                confirmButtonText: 'Cool'
                            }).then(() => {
                                window.location.href = '/web/assets/pages/accounts.html'
                            })
                            .catch((error) => {
                                console.log(error);
                                window.location.href = '/web/assets/pages/accounts.html'
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
                    Swal.fire("Deletion cancelled", "", "info");
                }
            })
        }, logout() {
            axios.post('/api/logout').then(() => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")