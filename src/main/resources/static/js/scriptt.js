function deleteBook(id) {

			swal({
				title: "Are you sure want to delete book ??",
				icon: "warning",
				buttons: true,
				dangerMode: true,
			})
				.then((willDelete) => {
					if (willDelete) {

						window.location = "/deleteBook/" + id;

					} else {
						swal("Your book is still in your book list..!!");
					}
				});

		}

		console.log("script called now");