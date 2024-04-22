// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;

// This constant indicates the path to our backend server (change to your own)
const backendUrl = "https://dry-wave-47246.herokuapp.com";
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        document.getElementById("addCancel")?.addEventListener("click", (e) => { newEntryForm.clearForm(); });
        document.getElementById("addButton")?.addEventListener("click", (e) => { newEntryForm.submitForm(); });
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        (<HTMLInputElement>document.getElementById("newTitle")).value = "";
        (<HTMLInputElement>document.getElementById("newMessage")).value = "";
        ///////////////////////////////////////////////////////////////////////////////////
        // reset the UI
         (<HTMLElement>document.getElementById("editElement")).style.display = "none";
         (<HTMLElement>document.getElementById("addElement")).style.display = "none";
         (<HTMLElement>document.getElementById("showElements")).style.display = "block";


    }


    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        window.alert("Submit form called.");
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + (<HTMLInputElement>document.getElementById("newTitle")).value;
        let sub_idea = "" + (<HTMLInputElement>document.getElementById("newMessage")).value;
        let num_like=0;
        if (title === "" || sub_idea === "") {
            window.alert("Error: title or message is not valid");
            return;
        }

        // set up an AJAX POST. 
        // When the server replies, the result will go to onSubmitResponse
        const doAjax = async () => {
            await fetch(`${backendUrl}/postidea`, {
                method: 'POST',
                body: JSON.stringify({
                    title: title,
                    ideas: sub_idea,
                    likes: num_like
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                // If we get an "ok" message, return the json
                if (response.ok) {
                    return Promise.resolve(response.json());
                }
                // Otherwise, handle server errors with a detailed popup message
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then((data) => {
                newEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch((error) => {
                
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
            mainList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class NewEntryForm


// a global for the EditEntryForm of the program.  See newEntryForm for explanation
var editEntryForm: EditEntryForm;

/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditEntryForm {
    /**
     * To initialize the object, we say what method of EditEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    constructor() {
        document.getElementById("editCancel")?.addEventListener("click", (e) => { editEntryForm.clearForm(); });
        document.getElementById("editButton")?.addEventListener("click", (e) => { editEntryForm.submitForm(); });
        document.getElementById("editLikes")?.addEventListener("click", (e) => { editEntryForm.submitForm(); });
    }

    /**
     * init() is called from an AJAX GET, and should populate the form if and 
     * only if the GET did not have an error
     */
    init(data: any) {
        // If we get an "ok" message, fill in the edit form
        if (data.mStatus === "ok") {
            (<HTMLInputElement>document.getElementById("editTitle")).value = data.mData.title;
            (<HTMLInputElement>document.getElementById("editMessage")).value = data.mData.mContent;
            (<HTMLInputElement>document.getElementById("editId")).value = data.mData.id;
            (<HTMLInputElement>document.getElementById("editCreated")).value = data.mData.mCreated;
            //////////////////////////////////////////////////////////////////
            // show the edit form
            (<HTMLElement>document.getElementById("editElement")).style.display = "block";
            (<HTMLElement>document.getElementById("editElement")).style.display = "block";
            (<HTMLElement>document.getElementById("showElements")).style.display = "none";
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }

    /**
     * Clear the form's input fields
     */
    clearForm() {
        (<HTMLInputElement>document.getElementById("editTitle")).value = "";
        (<HTMLInputElement>document.getElementById("editMessage")).value = "";
        (<HTMLInputElement>document.getElementById("editId")).value = "";
        (<HTMLInputElement>document.getElementById("editCreated")).value = "";
        ////////////////////////////////////////////////////////////////////////
         // reset the UI
         (<HTMLElement>document.getElementById("editElement")).style.display = "none";
         (<HTMLElement>document.getElementById("addElement")).style.display = "none";
         (<HTMLElement>document.getElementById("showElements")).style.display = "block";
    }

    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    submitForm() {
        window.alert("Submit edit form called.");
        // get the values of the two fields, force them to be strings, and check
        // that neither is empty
        let title = "" + (<HTMLInputElement>document.getElementById("editTitle")).value;
        let msg = "" + (<HTMLInputElement>document.getElementById("editMessage")).value;
        // NB: we assume that the user didn't modify the value of editId
        let id = "" + (<HTMLInputElement>document.getElementById("editId")).value;
        if (title === "" || msg === "" || id === "") {
            window.alert("Error: title, message, or id is not valid");
            return;
        }

        // set up an AJAX PUT.
        // When the server replies, the result will go to onSubmitResponse
        const doAjax = async () => {
            await fetch(`${backendUrl}/ideas/idea/${id}`, {
                method: 'PUT',
                body: JSON.stringify({
                    title: title,
                    ideas: msg
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                // If we get an "ok" message, return the json
                if (response.ok) {
                    // return response.json();
                    return Promise.resolve(response.json());
                }
                // Otherwise, handle server errors with a detailed popup message
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                // return response;
                return Promise.reject(response);
            }).then((data) => {
                editEntryForm.onSubmitResponse(data);
                console.log(data);
            }).catch((error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    private onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            editEntryForm.clearForm();
            mainList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
} // end class EditEntryForm


// a global for the main ElementList of the program.  See newEntryForm for 
// explanation
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
const arr:any[]=[];
class ElementList  {
    /**
     * refresh is the public method for updating messageList
     */
    refresh() {
        // Issue an AJAX GET and then pass the result to update(). 
        const doAjax = async () => {
            await fetch(`${backendUrl}/ideas`, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                // If we get an "ok" message, clear the form
                if (response.ok) {
                    return Promise.resolve(response.json());
                }
                // Otherwise, handle server errors with a detailed popup message
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then((data) => {
                mainList.update(data);

                console.log(data);
            }).catch((error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }
/**
 * sets functionality to the buttons displayed on the table 
 * @param data 
 */
    private update(data: any) {
        let elem_messageList = document.getElementById("messageList");

        if (elem_messageList !== null) {
            elem_messageList.innerHTML = "";

            let fragment = document.createDocumentFragment();
            let table = document.createElement('table');

            for (let i = 0; i < data.mData.length; ++i) {
                let tr = document.createElement('tr');
                let td_title = document.createElement('td');
                let td_id = document.createElement('td');
                let td_idea = document.createElement('td');
                let td_likes = document.createElement('td');
                td_title.innerHTML = data.mData[i].title;
                td_id.innerHTML = data.mData[i].id;
                td_idea.innerHTML=data.mData[i].ideas;
                td_likes.innerHTML=data.mData[i].likes;

                tr.appendChild(td_id);
                tr.appendChild(td_title);
                tr.appendChild(td_idea);
                tr.appendChild(this.buttons(data.mData[i].id));
                tr.appendChild(td_likes);
                table.appendChild(tr);
            }
            fragment.appendChild(table);
            elem_messageList.appendChild(fragment);

            // Find all of the delete buttons, and set their behavior
            const all_delbtns = (<HTMLCollectionOf<HTMLInputElement>>document.getElementsByClassName("delbtn"));
            for (let i = 0; i < all_delbtns.length; ++i) {
                all_delbtns[i].addEventListener("click", (e) => { mainList.clickDelete(e); });
            }
            // Find all of the edit buttons, and set their behavior
            const all_editbtns = (<HTMLCollectionOf<HTMLInputElement>>document.getElementsByClassName("editbtn"));
            for (let i = 0; i < all_editbtns.length; ++i) {
                all_editbtns[i].addEventListener("click", (e) => { mainList.clickEdit(e); });
            }

             // Find all of the edit buttons, and set their behavior
            // const arr:any[]=[];
            //let counter = 0;
            const all_likesbtns = (<HTMLCollectionOf<HTMLInputElement>>document.getElementsByClassName("likebtn"));
            // handles like/unlike by checking if the user has previously clicked and stores that click on local array arr
             for (let i = 0; i < all_likesbtns.length; ++i) {
                 all_likesbtns[i].addEventListener("click", (e) => { 
                    console.log(all_likesbtns[i])
                    console.log('inside like bttn');
                    let temp =(<HTMLElement>e.target).getAttribute("data-value");
                    // console.log('temp '+temp);
                    // console.log('array length '+ arr.length);
                    if (arr.some(e => e.cId === temp)) {
                        console.log('found in array') // can probably stucture this better 
                    }else{
                        console.log("not found in array" )
                        const instClick = {
                            cId: temp,
                            cLiked: 0
                        }
                        arr.push(instClick);
                    }
                    for(let j=0;j<arr.length;j++){
                        if(arr[j].cLiked===0 && arr[j].cId===temp){//add like
                            // console.log("inside if" )
                            // console.log("cId "+arr[j].cId)
                            // console.log("cLiked "+arr[j].cLiked)
                            arr[j].cLiked=1;
                            // console.log("cLiked"+arr[j].cLiked)
                            all_likesbtns[i].style.backgroundColor = `#8E44AD`;
                            mainList.clickLike(e,1); 
                            break;
                        }else if(arr[j].cLiked===1 && arr[j].cId===temp){//unlike
                            // console.log("inside if-else" )
                            // console.log(arr[j].cLiked);
                            // console.log(arr[j].cId),
                            // console.log("cLiked"+arr[j].cLiked)
                            arr[j].cLiked=0;
                            // console.log("cLiked"+arr[j].cLiked)
                            all_likesbtns[i].style.backgroundColor = `#FF4CAE`;
                            mainList.clickLike(e,0); 
                            break;
                        }
                    }
                    // console.log('array length '+ arr.length);
                    console.log('end like click')
                });
             }


        }
    }

   /**
    * clickDelete is the code we run in response to a click of a delete button
    * @param e 
    */
    private clickDelete(e: Event) {
        const id = (<HTMLElement>e.target).getAttribute("data-value");

        // Issue an AJAX DELETE and then invoke refresh()
        const doAjax = async () => {
            await fetch(`${backendUrl}/ideas/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                if (response.ok) {
                    return Promise.resolve(response.json());
                }
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then((data) => {
                mainList.refresh();
                console.log(data);
            }).catch((error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }
        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);

        // TODO: we've reapeated the same pattern 3+ times now, so we should really
        //       think about refactoring and abstracting this boilerplate into something
        //       easier to reuse, if possible 
    }

    
   /**
    * clickEdit is the code we run in response to a click of a delete button
    * @param e 
    */
    private clickEdit(e: Event) {
        // as in clickDelete, we need the ID of the row
        const id = (<HTMLElement>e.target).getAttribute("data-value");

        // Issue an AJAX GET and then pass the result to editEntryForm.init()
        const doAjax = async () => {
            await fetch(`${backendUrl}/ideas/${id}`, {
                method: 'GET',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                if (response.ok) {
                    return Promise.resolve(response.json());
                }
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            }).then((data) => {
                editEntryForm.init(data);
                console.log(data);
            }).catch((error) => {
                console.warn('Something went wrong.', error);
                window.alert("Unspecified error");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

    /**
     * handles the event of clicking like button and call put method to increment or decrement likes 
     * @param e on click event 
     * @param tf 1 to increment; 0 to decrement 
     */
    private clickLike(e: Event, tf) {
        // as in clickDelete, we need the ID of the row
        const id = (<HTMLElement>e.target).getAttribute("data-value");
        console.log("tf: "+tf)
        // Issue an AJAX GET and then pass the result to editEntryForm.init()
        const doAjax = async () => {
            await fetch(`${backendUrl}/likes/${id}/${tf}`, {
                method: 'PUT',
                headers: {
                    'Content-type': 'application/json; charset=UTF-8'
                }
            }).then((response) => {
                if (response.ok) {
                    return Promise.resolve(response.json());
                }
                else {
                    window.alert(`The server replied not ok: ${response.status}\n` + response.statusText);
                }
                return Promise.reject(response);
            })
            .then((data) => {
                mainList.refresh();
                console.log(data);

            })
            .catch((error) => {
                console.warn('Something went wrong in Put Likes.', error);
                window.alert("Unspecified error at likes put");
            });
        }

        // make the AJAX post and output value or error message to console
        doAjax().then(console.log).catch(console.log);
    }

   /**
    * buttons() adds a 'delete' button and an 'like' button to the HTML for each row
    * @param id 
    * @returns 
    */
    private buttons(id: string): DocumentFragment {
        let fragment = document.createDocumentFragment();
        let td = document.createElement('td');

        // create edit button, add to new td, add td to returned fragment
        let btn = document.createElement('button');
        // btn.classList.add("editbtn");
        // btn.setAttribute('data-value', id);
        // btn.innerHTML = 'Edit';
        // td.appendChild(btn);
        // fragment.appendChild(td);

        // create delete button, add to new td, add td to returned fragment
        td = document.createElement('td');
        btn = document.createElement('button');
        btn.classList.add("delbtn");
        btn.setAttribute('data-value', id);
        btn.innerHTML = 'Delete';
        td.appendChild(btn);
        fragment.appendChild(td);
        
        btn = document.createElement('button');
        btn.classList.add("likebtn");
        btn.setAttribute('data-value', id);
        btn.innerHTML = 'LIKE';
        td.appendChild(btn);
        fragment.appendChild(td);
        return fragment;
    }
} // end class ElementList

// Run some configuration code when the web page loads

document.addEventListener('DOMContentLoaded', () => {
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from the server
    mainList = new ElementList();

   // set up initial UI state
   (<HTMLElement>document.getElementById("editElement")).style.display = "none";
   (<HTMLElement>document.getElementById("addElement")).style.display = "none";
   (<HTMLElement>document.getElementById("showElements")).style.display = "block";

    // set up the "Add Message" button
    document.getElementById("showFormButton")?.addEventListener("click", (e) => {
        (<HTMLElement>document.getElementById("addElement")).style.display = "block";
        (<HTMLElement>document.getElementById("showElements")).style.display = "none";
    });

    mainList.refresh();
    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();
    window.alert('DOMContentLoaded');
}, false);
