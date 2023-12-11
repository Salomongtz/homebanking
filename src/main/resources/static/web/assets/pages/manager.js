const { createApp } = Vue

const app = createApp({
    data() {

        return {
            // clients: [{ id: 1, name: "Alice", lastName: "Smith", email: "alice@example.com" }, { id: 2, name: "Bob", lastName: "Jones", email: "bob@example.com" }, { id: 3, name: "Charlie", lastName: "Brown", email: "charlie @example.com" }, { id: 4, name: "David", lastName: "Green", email: "david @example.com" }, { id: 5, name: "Eve", lastName: "White", email: "eve @example.com" },],
            clients: [],
            firstName: "",
            lastName: "",
            client: {},
            id: 0
        }


    },
    created() {
        this.loadData()
    },
    methods: {

        loadData() {
            console.log("LoadData");
            axios("/api/clients")
                .then(response => {
                    (this.clients = response.data)
                    console.log(this.clients)
                })
                .catch(error => console.log(error))
        }, addClient() {
            if (this.clients.some(client => client.email === this.email)) {
                alert("Client already exists")
            } else if (this.firstName === "" || this.lastName === "" || this.email === "") {
                alert("Please fill in all fields")
            }
            else {
                this.createClient()
            }
        },
        // updateName() {
        //     axios.patch("clients/7", {
        //         "firstName": "Alice",
        //     }).then(response => {
        //         this.loadData()
        //     }).catch(error => {
        //         console.log(error)
        //     })
        // }, updateClient() {
        //     axios.patch("clients/6", {
        //         "firstName": "Mary",
        //         "lastName": "Jane",
        //         "email": "mjwjournalist@example.com"
        //     }).then(response => {
        //         this.loadData()
        //     }).catch(error => {
        //         console.log(error)
        //     })
        // },
        createClient() {
            axios.post("clients", {
                "firstName": this.firstName,
                "lastName": this.lastName,
                "email": this.email
            }).then(this.loadData()).catch(error => {
                console.log(error)
            })
        },
        deleteClient() {
            axios.delete("clients/" + this.id)
                .then(this.loadData())
                .catch(error => {
                    console.log(error)
                })
        }
    }
})
app.mount("#app")