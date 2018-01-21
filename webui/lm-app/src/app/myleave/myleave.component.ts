import { Component, OnInit } from '@angular/core';
import { LeaveDetails} from '../leavedetails';
import {Router,ActivatedRoute} from '@angular/router';
import {Employee} from '../employee';
import {EmployeeService} from "../employee.service";
import {Response} from '@angular/http';
import { SharedService } from '../services.service';


@Component({
  selector: 'app-myleave',
  templateUrl: './myleave.component.html',
  styleUrls: ['./myleave.component.css'],
  providers:[EmployeeService]
})
export class MyleaveComponent implements OnInit {
  message:String;
  employee:Employee;
  empId;
  show:boolean = false;
  showElement:boolean;
  ss;
  leaveDetails: LeaveDetails[];
  changeVar:boolean = false;;
    constructor(private router: Router,public employeeService:EmployeeService,private route: ActivatedRoute, ss:SharedService) {
      this.ss=ss;
      this.message='';
      this.employee= new Employee(this.empId);
      this.empId=localStorage.getItem('empId');
      
  
  
     }
doMyleave(): void {
      this.employeeService.doMyleave(this.empId).then(leaveDetails => {
        console.log('doMyleave promise resolved : ' + LeaveDetails.length);
        this.leaveDetails = leaveDetails;
      },
      err =>{
        console.log(err);
      }
    );
  
  }

  doApplyLeave() {
    //this.router.navigate(['/apply']);
    this.ss.change(this.changeVar);
    this.show = true;
    //this.showElement=true;
    //localStorage.setItem('showElement',JSON.stringify(this.showElement));
  }
  ngOnInit(){
  this.LeaveDetails();
  }
  LeaveDetails():void {
    this.employeeService.doMyleave(this.empId).then(leaveDetails => {
      this.leaveDetails= leaveDetails;
    });
  }
  }
  