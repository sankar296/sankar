import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
//import { Apply} from '../services/app.applyleave.service'
import { EmployeeService } from '../employee.service';
import {LeaveDetails } from '../leavedetails';
import { SharedService} from '../services.service';

@Component({
  selector: 'app-applyleave',
  templateUrl: './applyleave.component.html',
  styleUrls: ['./applyleave.component.css']
})
export class ApplyleaveComponent implements OnInit {
  
  message:String;
  leavedetails:LeaveDetails;
  empId;
  ss;
  showElement;
  changeVar;
  constructor(public empservice:EmployeeService, public router: Router, ss:SharedService) { 
    this.ss=ss;
    this.message='';
    this.leavedetails=new LeaveDetails();
    this.empId = localStorage.getItem('empId');
  }

 doApply(){
    this.empservice.doApply(this.empId,this.leavedetails).subscribe(
      data => {
        this.message=data;
        //alert(this.message);
        this.showElement = true;
        this.ss.change(this.showElement);
        this.router.navigate(['/dashboard']);
      },
      err => {
        this.message='leave not applied';
        console.log(err);
      });

  }
  doCancel() {
    this.showElement = true;
    this.ss.change(this.showElement);
    this.router.navigate(['/dashboard']);
    
    //console.log(this.changeVar);
  }
  ngOnInit() {
  
  }

}
