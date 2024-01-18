const { createApp } = Vue

createApp({
    data() {
        return {
            client: {},
            cards: [],
            credit: [],
            debit: [],
            condicion: true,
        }
    },
    created() {
        this.loadData()
        this.isCardExpired("2029-01-16")
    },
    methods: {
        loadData() {
            console.log("LoadData");
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.cards = this.client.cards
                    this.debit = this.cards.filter(card => card.type === "DEBIT").toSorted((a, b, c) => a.color.localeCompare(b.color) || a.cardHolder.localeCompare(c.cardHolder))
                    this.credit = this.cards.filter(card => card.type === "CREDIT")
                    console.log(this.client)
                    console.log(this.client.cards);
                })
                .catch(error => {
                    console.log(error)
                })
        },
        deleteCard(id) {
            Swal.fire({
                title: "Are you sure?",
                html: `
                <div class="text-center text-white">
                  <strong>Loan Type:</strong> This card will be deleted<br>
                  <strong>Card Number:</strong> ${id} <br>
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
                    axios.patch(`/api/clients/current/cards/${id}`)
                        .then((response) => {
                            console.log(response)
                            Swal.fire({
                                title: 'Success!',
                                text: 'Card deleted!',
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
                    Swal.fire("Deletion cancelled", "", "info");
                }
            })
        },
        isCardExpired(thruDate) {
            const currentDate = new Date();
            const cardDate = new Date(thruDate);
            return cardDate < currentDate;
        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")