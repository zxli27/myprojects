let profile=document.querySelector(".profilecontainer");
let skill=document.querySelector(".skillcontainer");
let project=document.querySelector(".projectcontainer");

profile.style.display="none";
skill.style.display="none";
project.style.display="none";

document.querySelector(".list-group").addEventListener("click",function(e){
  if(e.target.className=="btn profile-btn"){
    if(profile.style.display==="block"){
      profile.style.display="none";
    }
    else{
      profile.style.display="block";
    }
  }
  else if(e.target.className=="btn skill-btn"){
    if(skill.style.display==="block"){
      skill.style.display="none";
    }
    else{
      skill.style.display="block";
    }
  }
  else if(e.target.className=="btn project-btn"){
    if(project.style.display==="block"){
      project.style.display="none";
    }
    else{
      project.style.display="block";
    }
  }

  e.preventDefault();
})