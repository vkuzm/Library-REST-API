<h2>LIBRARY REST API EXAMPLE</h2>
<p>A simple example of REST API with Spring Framework & Hibernate.</p>
<p><h4>API url:</h4><code>http://localhost:8080/api/</code></p>


<h2>API resources</h2>

<h3>Books</h3>
<p>List of books</p>
<table>
<thead>
<tr>
<th align="left">Method</th>
<th align="left">Resource</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/books</code></td>
<td align="left">Returns list of books</td>
</tr>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/books/{book_id}</code></td>
<td align="left">Returns book by ID</td>
</tr>
<tr>
<td align="left"><code>POST</code></td>
<td align="left"><code>/books</code></td>
<td align="left">Create a book</td>
</tr>
<tr>
<td align="left"><code>PUT</code></td>
<td align="left"><code>/books/{book_id}</code></td>
<td align="left">Update a book</td>
</tr>
<tr>
<td align="left"><code>POST</code></td>
<td align="left"><code>/books/{book_id}/addComment</code></td>
<td align="left">Add a comment</td>
</tr>
<tr>
<td align="left"><code>DELETE</code></td>
<td align="left"><code>/books/{book_id}</code></td>
<td align="left">Delete a book</td>
</tr>
</tbody>
</table>

<h3>Genres</h3>
<p>List of genres</p>
<table>
<thead>
<tr>
<th align="left">Method</th>
<th align="left">Resource</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/genres</code></td>
<td align="left">Returns list of genres</td>
</tr>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/genres/{genre_id}</code></td>
<td align="left">Returns genre by ID</td>
</tr>
<tr>
<td align="left"><code>POST</code></td>
<td align="left"><code>/genres</code></td>
<td align="left">Create a genre</td>
</tr>
<tr>
<td align="left"><code>PUT</code></td>
<td align="left"><code>/genres/{genre_id}</code></td>
<td align="left">Update a genres</td>
</tr>
<tr>
<td align="left"><code>DELETE</code></td>
<td align="left"><code>/genres/{genre_id}</code></td>
<td align="left">Delete a genre</td>
</tr>
</tbody>
</table>

<h3>Authors</h3>
<p>List of authors</p>
<table>
<thead>
<tr>
<th align="left">Method</th>
<th align="left">Resource</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/authors</code></td>
<td align="left">Returns list of authors</td>
</tr>
<tr>
<td align="left"><code>GET</code></td>
<td align="left"><code>/authors/{author_id}</code></td>
<td align="left">Returns author by ID</td>
</tr>
<tr>
<td align="left"><code>POST</code></td>
<td align="left"><code>/authors</code></td>
<td align="left">Create a author</td>
</tr>
<tr>
<td align="left"><code>PUT</code></td>
<td align="left"><code>/authors/{author_id}</code></td>
<td align="left">Update a authors</td>
</tr>
<tr>
<td align="left"><code>DELETE</code></td>
<td align="left"><code>/authors/{author_id}</code></td>
<td align="left">Delete a author</td>
</tr>
</tbody>
</table>


<h2>Testing</h2>

<p>
<h4>Get list of books:</h4>
<code>curl --location --request GET "http://localhost:8080/api/books</code>
</p>
<p>
<h4>Get list of genres:</h4>
<code>curl --location --request GET "http://localhost:8080/api/genres"</code>
</p>
<p>
<h4>Get list of authors:</h4>
<code>curl --location --request GET "http://localhost:8080/api/authors"</code>
</p>
<p>
<h4>Get a book by ID:</h4>
<code>curl --location --request GET"http://localhost:8080/api/books/1</code>
</p>
<p>
<h4>Get a genre by ID:</h4>
<code>curl --location --request GET "http://localhost:8080/api/genres/1"</code>
</p>
<p>
<h4>Get an author by ID</h4>
<code>curl --location --request GET "http://localhost:8080/api/authors/1"</code>
</p>

<p>
<h4>Add a book</h4>
<code>curl --location --request POST "http://localhost:8080/api/books/" \
  --header "Content-Type: application/json" \
  --data "  {
        \"name\": \"Book test\",
        \"author\": {
            \"id\": 1,
            \"name\": \"Max Black\"
        },
        \"genre\": {
            \"id\": 1,
            \"name\": \"Fiction\"
        },
        \"comments\": [
            {
                \"id\": 1,
                \"author\": \"Igor\",
                \"comment\": \"This is insane!\"
            }
        ]
    }"</code>
</p>
<p>
<h4>Add a genre</h4>
<code>curl --location --request POST "http://localhost:8080/api/genres" \
  --header "Content-Type: application/json" \
  --data "{\"name\": \"Genre name\"}""</code>
</p>
<p>
<h4>Add an author</h4>
<code>curl --location --request POST "http://localhost:8080/api/authors/" \
  --header "Content-Type: application/json" \
  --data "{\"name\": \"Author 1\"}"</code>
</p>
<p>
<h4>Add a comment</h4>
<code>curl --location --request POST "http://localhost:8080/api/books/1/addComment" \
  --header "Content-Type: application/json" \
  --data "{
\"author\": \"Mice\",
\"comment\": \"It is nice!\"
}"</code>
</p>
