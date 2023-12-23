const { createApp } = Vue

createApp({
    data() {
        return {
            email: "",
            password: "",
            firstName: "",
            lastName: "",
            rememberMe: false,
            showLogin: true
        }
    },
    created() {
    },
    methods: {
        login() {
            console.log(this.email)
            console.log(this.password)
            axios.post("/api/login?email=" + this.email + "&password=" + this.password)
                .then(response => {
                    console.log('Signed in! Welcome' + this.email + response.data)
                    this.clearData()
                    window.location.href = './assets/pages/accounts.html'
                }).catch(
                    error => {
                        console.log(error)
                        Swal.fire({
                            icon: "error",
                            title: "Oops...",
                            text: "Something went wrong!",
                            footer: '<a href="#">Why do I have this issue?</a>'
                        })
                    })
        },
        register() {
            axios.post("/api/clients?firstName=" + this.firstName + "&lastName=" + this.lastName + "&email=" + this.email + "&password=" + this.password)
                .then(response => {
                    console.log("registered" + this.email);
                    console.log(response.data);
                    this.login();
                })
                .catch(error => console.log(error))
        },
        clearData() {
            this.email = ""
            this.password = ""
            this.firstName = ""
            this.lastName = ""
        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    },
    computed: {
        test() {
            console.log(this.email);
            console.log(this.password);
        }
    }
}
).mount("#app")