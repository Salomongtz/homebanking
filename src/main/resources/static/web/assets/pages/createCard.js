const { createApp } = Vue

createApp({
    data() {
        return {
            color: "",
            type: "",
        }
    },
    created() {
    },
    methods: {
        createCard() {
            axios.post("/api/clients/current/cards?type=" + this.type.toUpperCase() + "&color=" + this.color.toUpperCase())
                .then((response) => {
                    console.log(response)
                    Swal.fire({
                        title: 'Success!',
                        text: 'Your ' + this.color + ' ' + this.type + ' card has been created!',
                        icon: 'success',
                        confirmButtonText: 'Cool'
                    })
                })
                .catch((error) => {
                    let msg = ""
                    if (error.response.data != "") {
                        msg = 'You already have a ' + this.color + ' ' + this.type + ' card.'
                    }
                    else {
                        msg = 'Please fill all the fields.'
                    }
                    Swal.fire({
                        title: 'Error!',
                        text: msg,
                        icon: 'error',
                        confirmButtonText: 'Go back'
                    })
                    console.log(error)
                })
        },
        logout() {
            axios.post('/api/logout').then(response => window.location.href = '/web/index.html')
        }
    }
}
).mount("#app")