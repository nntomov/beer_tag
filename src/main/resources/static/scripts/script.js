$('#btnReg').on('click', function () {
    $('#regForm').css('display', 'block');
    $('.forms').css({width: '100%', height: '1000px', background: 'rgba(0,0,0, 0.8)'})
    $('.form-container').css('display', 'block');
});
$('#btnSignIn').on('click', function () {
    $('#signInForm').css('display', 'block');
    $('.forms').css({width: '100%', height: '1000px', background: 'rgba(0,0,0, 0.8)'})
    $('.form-container').css('display', 'block');
});

$('.close-form').on('click', function () {
    $('.forms').css({width: '0', height: '0'})
    $('#signInForm').css('display', 'none');
    $('#regForm').css('display', 'none');
    $('.form-container').css('display', 'none');

});

$('.carousel .item').each(function () {
    var next = $(this).next();
    if (!next.length) {
        next = $(this).siblings(':first');
    }
    next.children(':first-child').clone().appendTo($(this));

    for (var i = 0; i < 2; i++) {
        next = next.next();
        if (!next.length) {
            next = $(this).siblings(':first');
        }
        next.children(':first-child').clone().appendTo($(this));
    }
});
// beer info rating
$('.rating span i').on('mouseover', function () {
    if ($(this).attr('class') == 'far fa-star') {
        var starId = $(this).attr('id'),
            starNum = starId.replace('star-', '');
        for (var i = 1; i <= starNum; i++) {
            $('#star-' + i).attr('class', 'fas fa-star rated')
        }
    }
});
$('.rating span i').on('mouseout', function () {
    if ($(this).attr('class') == 'fas fa-star rated') {
        $(this).attr('class', 'far fa-star');
    }
});
$('.rating').on('mouseout', function () {
    if ($('.rating span i').attr('class') == 'fas fa-star rated') {
        $('.rating span i').attr('class', 'far fa-star');
    }
});
// beer add tag
$('#add-tag').on('click', function (e) {
    e.preventDefault();
    $('#raddTagForm').css('display', 'block');
    $('.forms').css({width: '100%', height: '1000px', background: 'rgba(0,0,0, 0.8)'})
    $('.form-container').css('display', 'block');
});

// USER INFO SLIDERS
/* first carousel slidesshow */
$('#carousel-1').carousel({
    // Amount of time to delay between cycling slide, If false, no cycle
    interval: 500,

    // Pauses slide on mouse enter and resumes on mouseleave.
    pause: "hover",

    // Whether carousel should cycle continuously or have hard stops.
    wrap: true,

    // Whether the carousel should react to keyboard events.
    keyboard: true
});


/* second carousel slidesshow */
$('#myCarousel').carousel({
    interval: 4000,
    // Pauses slide on mouse enter and resumes on mouseleave.
    pause: "hover",
    // Whether carousel should cycle continuously or have hard stops.
    wrap: true,

});


/* third carousel slidesshow */
$('#myCarousel-1').carousel({
    interval: 4000,
    // Pauses slide on mouse enter and resumes on mouseleave.
    pause: "hover",
    // Whether carousel should cycle continuously or have hard stops.
    wrap: true,
});

$(document).ready(function () {
    beer.initialize();
    user.initialize();

    // Table counter..
    var counter = 1;

    $('tr.user-row > td > span.users-counter').each(function () {
        $(this).text(counter);
        counter++;
    });
});

var beer = {
    initialize: function () {
        beer.addBeerEvents();
    },
    addBeerEvents: function () {

        $('a#wish-button').on('click', function () {
            var beerId = $(this).attr('data-beer-id');
            var userId = $(this).attr('data-user-id');
            var type = $(this).attr('data-type');
            beer.setBeerType(beerId, userId, type);
            console.log("wish button hit")
        });

        $('a#drank-button').on('click', function () {
            var beerId = $(this).attr('data-beer-id');
            var userId = $(this).attr('data-user-id');
            var type = $(this).attr('data-type');
            beer.setBeerType(beerId, userId, type);
        });

        $('a#delete-beer').on('click', function (event) {
            event.preventDefault();
            var beerId = $(this).attr('data-beer-id');
            beer.deleteBeer(beerId);
        })
    },
    setBeerType: function (beerID, userID, type) {
        $.post('/api/user/set-type', {
            beerID: beerID,
            userID: userID,
            type: type
        }).done(function () {
            $('div#cheers-modal').modal('show');
            setTimeout(function () {
                $('div#cheers-modal').modal('hide');
            }, 2000)
        });
    },
    deleteBeer: function (id) {
        $.ajax({
            url: '/api/beer/delete?' + $.param({
                beerId: id
            }),
            type: 'DELETE'
        })
    }
};


var user = {
    initialize: function () {
        user.addUserEvents()
    },
    addUserEvents: function () {
        $('a#delete-user').on('click', function (event) {
            event.preventDefault();
            var userId = $(this).attr('data-user-id');
            user.deleteUser(userId);
            $(this).closest('tr').remove();
        })
    },
    deleteUser: function (id) {
        $.ajax({
            url: '/api/user/delete?' + $.param({
                userId: id
            }),
            type: 'DELETE'
        })
    }
};