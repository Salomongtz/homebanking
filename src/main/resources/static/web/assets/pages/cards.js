const { createApp } = Vue

createApp({
    data() {
        return {
            client: {},
            cards: [],
            credit: [],
            debit: [],
            condicion:true
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
                    this.cards = this.client.cards
                    this.debit = this.cards.filter(card => card.type === "DEBIT").toSorted((a, b, c) => a.color.localeCompare(b.color) || a.cardHolder.localeCompare(c.cardHolder))
                    this.credit = this.cards.filter(card => card.type === "CREDIT")
                    console.log(this.client)
                    console.log(this.client.cards);
                })
                .catch(error => console.log(error))
        }
    }
}).mount("#app")