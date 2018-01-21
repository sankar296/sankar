import { Component, OnInit } from '@angular/core';
import { SharedService } from '../services.service';
import { SharedServices } from '../setservice.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  empId;
  ss;
  ss1;
  showElement:boolean;
  showElements:boolean;
  constructor(ss: SharedService, ss1: SharedServices) { 
    this.empId = localStorage.getItem('empId');
    //this.showElement=JSON.parse(localStorage.getItem('showElement'));
    console.log(this.showElement+"~~~~");
    //this.showElement = false;
    this.ss = ss;
    this.ss1 = ss1;
    //this.showItem
    //alert(this.showElement);
  }
 
  ngOnInit() {
    this.ss.getEmittedValue()
    .subscribe(item => this.showElement=item);
    this.ss1.getEmittedValue()
    .subscribe(item => this.showElements=item);
  }

}
