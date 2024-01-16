const { createApp } = Vue

createApp({
    data() {
        return {
            client: {},
            cards: [],
            credit: [],
            debit: [],
            condicion: true,
            today: new Date()
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
                    console.log(this.formatDate(this.today));
                })
                .catch(error => {
                    console.log(error)
                })
        },
        deleteCard(id) {
            axios.patch(`/api/clients/current/cards/${id}`)
                .then(response => {
                    console.log(response)
                    this.loadData()
                })
                .catch(error => {
                    console.log(error)
                })
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        },
        isCardExpired(thruDate) {
            const currentDate = new Date();
            const cardDate = new Date(thruDate);
            console.log(currentDate);
            console.log(cardDate);
            console.log(cardDate < currentDate);
            return cardDate < currentDate;
        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}).mount("#app")