import { Component, OnInit } from '@angular/core';
import { UsersService } from '../admin/users/users.service';

@Component({
  selector: 'app-authors',
  templateUrl: './authors.component.html',
  styleUrls: ['./authors.component.css']
})
export class AuthorsComponent implements OnInit {

  authors: any;
  page = 0;

  constructor(private userService: UsersService) { }

  ngOnInit() {
    this.userService.getAuthors(this.page).subscribe((a: any) => this.authors = a.content);
  }

}
