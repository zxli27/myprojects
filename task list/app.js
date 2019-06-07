const form=document.querySelector("#task-form");
const list=document.querySelector(".collection");
const clearBtn=document.querySelector(".clear-tasks");
const filter=document.querySelector('#filter');
const taskinput=document.querySelector('#task');


//extract data from local storage 
document.addEventListener('DOMContentLoaded',getTasks);
function getTasks(){
  let tasks;
  if(localStorage.getItem('tasks')===null){
    tasks=[];
  }
  else{
    tasks=JSON.parse(localStorage.getItem('tasks'));
  }
  tasks.forEach(function(task){
    let li=document.createElement('li');
    li.className='collection-item';
    li.appendChild(document.createTextNode(task));
    let link=document.createElement('a');
    link.className='secondary-content delete-item';
    link.innerHTML='<i class="fa fa-remove"></i>';
    li.appendChild(link);
    list.appendChild(li);
  })
}

//form submit event
form.addEventListener('submit',addtask);

function addtask(e) {
  if(taskinput.value===''){alert("the input is empty");}
  else{
    let li=document.createElement('li');
    li.className='collection-item';
    li.appendChild(document.createTextNode(taskinput.value));
    let link=document.createElement('a');
    link.className='secondary-content delete-item';
    link.innerHTML='<i class="fa fa-remove"></i>';
    li.appendChild(link);
    list.appendChild(li);
    storeTaskInLocalStorage(taskinput.value);
    taskinput.value='';
  }
  e.preventDefault();
}

//store the value into local storage
function storeTaskInLocalStorage(item){
  let tasks;
  if(localStorage.getItem('tasks')===null){
    tasks=[];
  }
  else{
    tasks=JSON.parse(localStorage.getItem('tasks'));
  }
  tasks.push(item);
  localStorage.setItem('tasks',JSON.stringify(tasks));
}


//delete botton click event
list.addEventListener('click',deleteItem);

function deleteItem(e){
  if(e.target.className==='fa fa-remove'){
    e.target.parentNode.parentNode.remove();

    //remove from LS
    removeTaskfromLS(e.target.parentNode.parentNode);
  }
}

function removeTaskfromLS(task){
  let tasks;
  tasks=JSON.parse(localStorage.getItem('tasks'));
  let remove=tasks.splice(tasks.indexOf(task.textContent),1);
  localStorage.setItem('tasks',JSON.stringify(tasks));
}


//clear task button click event
clearBtn.addEventListener('click',cleartask);

function cleartask(e){
  while(list.firstElementChild){list.removeChild(list.firstElementChild);}
  localStorage.clear();
}

//filter keyup event
filter.addEventListener('keyup',search);

function search(e){
  let text=e.target.value;
  list.childNodes.forEach(function(item){
    if( item.firstChild.textContent.indexOf(text) >=0){
      item.style.display ='block';
    }
    else{item.style.display='none';}
  })
}

